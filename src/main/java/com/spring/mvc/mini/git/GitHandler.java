package com.spring.mvc.mini.git;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.util.FS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by n2zhang on 2015/7/31.
 */
@Component
public class GitHandler {

    static Logger LOGGER = LoggerFactory.getLogger(GitHandler.class);

    @Value("${path.git.ssh}")
    private String gitSshPath;

    @Value("${path.git.ssh.key}")
    private String gitSshKey;

    public void checkin() {

        File repositoryPath = new File(gitSshPath);
        Repository repository = null;
        try {
            repository = FileRepositoryBuilder.create(new File(repositoryPath, ".git"));
            Git git = new Git(repository);

            git.add()
                    .addFilepattern(".")
                    .call();

            git.commit()
                    .setMessage("new managed object commited")
                    .call();

            final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
                @Override
                protected void configure(OpenSshConfig.Host host, Session session) {
                    java.util.Properties config = new java.util.Properties();
                    config.put("StrictHostKeyChecking", "no");
                    session.setConfig(config);
                }

                @Override
                protected JSch createDefaultJSch(FS fs) throws JSchException {
                    JSch defaultJSch = super.createDefaultJSch(fs);
                    defaultJSch.addIdentity(gitSshKey);
                    return defaultJSch;
                }
            };

            Iterable resultIterable = git.push().setRemote(gitSshPath).setTransportConfigCallback(new TransportConfigCallback() {

                @Override
                public void configure(Transport transport) {
                    SshTransport sshTransport = (SshTransport) transport;
                    sshTransport.setSshSessionFactory(sshSessionFactory);
                }
            }).call();

            Iterator iterator = resultIterable.iterator();
            while (iterator.hasNext () ) {
                PushResult result = (PushResult) iterator.next ();
                LOGGER.info(result.getMessages());
            }

            LOGGER.info("Json SVN Checked in at " + new Date());
        } catch (IOException | UnmergedPathsException | WrongRepositoryStateException | ConcurrentRefUpdateException
                | NoFilepatternException | AbortedByHookException | NoHeadException | InvalidRemoteException
                | TransportException | NoMessageException e) {
            LOGGER.error(e.toString());
        } catch (GitAPIException e) {
            LOGGER.error(e.toString());
        } finally {
            repository.close();
        }

    }
}
