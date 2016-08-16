package com.spring.mvc.mini.svn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;


@Component
public class SVNHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SVNHandler.class);

    @Value("${svn.rep.url}")
    private String repURL;

    @Value("${path.rep}")
    private String repPath;

    @Value("${svn.username}")
    private String username;

    @Value("${svn.password}")
    private String password;

    @Value("${path.json}")
    private String jsonPath;

    @Value("${path.xml}")
    private String xmlPath;

    public void svnCheckin() {

        SVNRepository repository;

        SVNClientManager ourClientManager = SVNClientManager.newInstance();

        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(repURL));

            ISVNAuthenticationManager authManager =
                    SVNWCUtil.createDefaultAuthenticationManager(username, password);
            repository.setAuthenticationManager(authManager);

            LOG.info("Repository Root: " + repository.getRepositoryRoot(true));
            LOG.info("Repository UUID: " + repository.getRepositoryUUID(true));

            LOG.info("Repository Latest Revision: " + repository.getLatestRevision());

            ourClientManager.setAuthenticationManager(authManager);

            SVNCommitClient commitClient = ourClientManager.getCommitClient();
            SVNDiffClient diffClient = ourClientManager.getDiffClient();

            File xmlfile = new File(xmlPath);
            File jsonfile = new File(jsonPath);
            File[] xmlfilearray = {xmlfile};
            File[] jsonfilearray = {jsonfile};

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            diffClient.doDiff(xmlfile, SVNRevision.UNDEFINED, SVNRevision.BASE, SVNRevision.WORKING,
                    SVNDepth.INFINITY, true, outputStream, null);

            if (outputStream.size() > 0) {
                commitClient.doCommit(xmlfilearray, false, outputStream.toString(), false, true);
                LOG.info("XML Checked in at " + new Date());
            }

            diffClient.doDiff(jsonfile, SVNRevision.UNDEFINED, SVNRevision.BASE, SVNRevision.WORKING,
                    SVNDepth.INFINITY, true, outputStream, null);

            if (outputStream.size() > 0) {
                commitClient.doCommit(jsonfilearray, false, outputStream.toString(), false, true);
                LOG.info("Json SVN Checked in at " + new Date());
            }

        } catch (SVNException e) {
            LOG.error(e.toString());
        } finally {
            ourClientManager.dispose();
        }
    }

    public void svnCheckout() {

        File svnTempDir = new File(repPath);
        if (svnTempDir.exists()) {
            return;
        }

        SVNRepository repository;

        try {
            //initiate the reporitory from the url
            repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(repURL));

            //create authentication data
            ISVNAuthenticationManager authManager =
                    SVNWCUtil.createDefaultAuthenticationManager(username, password);
            repository.setAuthenticationManager(authManager);

            //need to identify latest revision
            long latestRevision = repository.getLatestRevision();

            //create client manager and set authentication
            SVNClientManager ourClientManager = SVNClientManager.newInstance();
            ourClientManager.setAuthenticationManager(authManager);

            //use SVNUpdateClient to do the export
            SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
            updateClient.setIgnoreExternals(false);

            updateClient.doCheckout(repository.getLocation(), svnTempDir,
                    SVNRevision.create(latestRevision), SVNRevision.create(latestRevision), true);

        } catch (SVNException e) {
            LOG.error(e.toString());
        } finally {
            LOG.info("resources/svn_temp checked outs");
        }
    }

}
