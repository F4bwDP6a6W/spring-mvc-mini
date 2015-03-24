package com.companyname.projectname.mocr.svn;

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

import com.companyname.projectname.mocr.properties.PropertiesBean;

@Component
public class SVNHandler {
	
	static Logger LOGGER = LoggerFactory.getLogger(SVNHandler.class);

    @Autowired
    private PropertiesBean propertiesBean;
	
	public void svnCheckin(){
		
		SVNRepository repository = null;
		
		SVNClientManager ourClientManager = SVNClientManager.newInstance();
		
		try{
			//initiate the reporitory from the url
			repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(propertiesBean.getRepurl()));
			
			//create authentication data
			ISVNAuthenticationManager authManager = 
					SVNWCUtil.createDefaultAuthenticationManager(propertiesBean.getUsername(), propertiesBean.getPassword());
			repository.setAuthenticationManager(authManager);
			
			//output some data to verify connection
			System.out.println( "Repository Root: " + repository.getRepositoryRoot( true ) );
			System.out.println(  "Repository UUID: " + repository.getRepositoryUUID( true ) );
			
			//need to identify latest revision
			long latestRevision = repository.getLatestRevision();
			System.out.println(  "Repository Latest Revision: " + latestRevision);
			
			//create client manager and set authentication
			
			ourClientManager.setAuthenticationManager(authManager);
			
			//use commitClient to do the export
			SVNCommitClient commitClient = ourClientManager.getCommitClient();
		    SVNDiffClient diffClient = ourClientManager.getDiffClient();
		    
		    File xmlfile = new File(propertiesBean.getXmlpath());
		    File jsonfile = new File(propertiesBean.getJsonpath());
		    File[] xmlfilearray = {xmlfile};
		    File[] jsonfilearray = {jsonfile};
		    
		    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    
		    diffClient.doDiff(xmlfile, SVNRevision.UNDEFINED, SVNRevision.BASE ,SVNRevision.WORKING,
		    		                        SVNDepth.INFINITY, true, outputStream, null);
	
		    if(outputStream.size() > 0){
		    	commitClient.doCommit(xmlfilearray, false,outputStream.toString() , false, true);
		    	LOGGER.info("XML Checked in at "+new Date());
		    }
			
		    diffClient.doDiff(jsonfile, SVNRevision.UNDEFINED, SVNRevision.BASE ,SVNRevision.WORKING,
                    SVNDepth.INFINITY, true, outputStream, null);

			if(outputStream.size() > 0){
				commitClient.doCommit(jsonfilearray, false,outputStream.toString() , false, true);
				LOGGER.info("Json SVN Checked in at "+new Date());
			}

		} catch (SVNException e) {
			LOGGER.error(e.toString());
		} finally {
			ourClientManager.dispose();
		}
	}
	
	public void svnCheckout() {
		
		File svnTempDir = new File(propertiesBean.getReppath());
		if (svnTempDir.exists()){
			return;
		}
		
		SVNRepository repository = null;
		
		try{
			//initiate the reporitory from the url
			repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(propertiesBean.getRepurl()));
			
			//create authentication data
			ISVNAuthenticationManager authManager = 
					SVNWCUtil.createDefaultAuthenticationManager(propertiesBean.getUsername(), propertiesBean.getPassword());
			repository.setAuthenticationManager(authManager);
			
			//need to identify latest revision
			long latestRevision = repository.getLatestRevision();
			
			//create client manager and set authentication
			SVNClientManager ourClientManager = SVNClientManager.newInstance();
			ourClientManager.setAuthenticationManager(authManager);
			
			//use SVNUpdateClient to do the export
			SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
			updateClient.setIgnoreExternals( false );
			
			updateClient.doCheckout(repository.getLocation(), svnTempDir,
					SVNRevision.create(latestRevision), SVNRevision.create(latestRevision), true);
			
		} catch (SVNException e) {
			LOGGER.error(e.toString());
		} finally {
			LOGGER.info("resources/svn_temp checked outs");
		}
	}

}
