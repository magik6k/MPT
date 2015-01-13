package net.magik6k.mpt;

import net.magik6k.jwwf.ace.AcePlugin;
import net.magik6k.jwwf.core.JwwfServer;
import net.magik6k.jwwf.oauth.JwwfOAuthPlugin;
import net.magik6k.mpt.db.PackageBase;
import net.magik6k.mpt.db.RepoBase;
import net.magik6k.mpt.db.UserBase;
import net.magik6k.mpt.plugin.MptPlugin;
import net.magik6k.mpt.rest.FileServlet;
import net.magik6k.mpt.rest.PackageServlet;
import net.magik6k.mpt.rest.UpdateServlet;

public class Main {
	public static void main(String[] args){
		int port;
		try {
			port = new Integer( args[ 0 ] );
		} catch ( Exception e ) {
			System.out.println( "No port specified. Defaulting to 8888" );
			port = 8888;
		}
		
		if(Settings.instance.getProperty("githubClientID") == "setThisValue"){System.out.println("Bad configutation");return;}
		if(Settings.instance.getProperty("githubClientSecret") == "setThisValue"){System.out.println("Bad configutation");return;}
		if(Settings.instance.getProperty("githubAuthRedirURL") == "setThisValue"){System.out.println("Bad configutation");return;}
		
		UserBase.instance = new UserBase();
		RepoBase.instance = new RepoBase();
		PackageBase.instance = new PackageBase();
		
		JwwfServer.debugOutput(false);
		new JwwfServer( port )
			.attachPlugin(new AcePlugin())
			.attachPlugin(new JwwfOAuthPlugin())
			.attachPlugin(new MptPlugin())
			.bindServlet(new PackageServlet(), "/api/package/*")
			.bindServlet(new FileServlet(), "/api/file/*")
			.bindServlet(new UpdateServlet(), "/api/update")
			.bindWebapp(MptClient.class).start();
		
	}
}
