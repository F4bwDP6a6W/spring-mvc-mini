package com.spring.mvc.mini.svn;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.spring.mvc.mini.properties.Properties;

@Component
public class SVNHandler {

    static Logger LOG = LoggerFactory.getLogger(SVNHandler.class);

    @Autowired
    private Properties properties;

    public void svnCheckin() {

        SVNRepository repository = null;

        SVNClientManager ourClientManager = SVNClientManager.newInstance();

        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(properties.getRepURL()));

            ISVNAuthenticationManager authManager =
                    SVNWCUtil.createDefaultAuthenticationManager(properties.getUsername(), properties.getPassword());
            repository.setAuthenticationManager(authManager);

            LOG.info("Repository Root: " + repository.getRepositoryRoot(true));
            LOG.info("Repository UUID: " + repository.getRepositoryUUID(true));

            LOG.info("Repository Latest Revision: " + repository.getLatestRevision());

            ourClientManager.setAuthenticationManager(authManager);

            SVNCommitClient commitClient = ourClientManager.getCommitClient();
            SVNDiffClient diffClient = ourClientManager.getDiffClient();

            File xmlfile = new File(properties.getXmlPath());
            File jsonfile = new File(properties.getJsonPath());
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

        File svnTempDir = new File(properties.getRepPath());
        if (svnTempDir.exists()) {
            return;
        }

        SVNRepository repository = null;

        try {
            //initiate the reporitory from the url
            repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(properties.getRepURL()));

            //create authentication data
            ISVNAuthenticationManager authManager =
                    SVNWCUtil.createDefaultAuthenticationManager(properties.getUsername(), properties.getPassword());
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
