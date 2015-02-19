package net.magik6k.mpt.view;

import net.magik6k.jwwf.ace.AceEditor;
import net.magik6k.jwwf.ace.AceMode;
import net.magik6k.jwwf.ace.AceTheme;
import net.magik6k.jwwf.enums.PanelAlign;
import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.input.InternalLink;
import net.magik6k.jwwf.widgets.basic.panel.HorizontalPanel;
import net.magik6k.jwwf.widgets.basic.panel.VerticalPanel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.db.PackageBase;
import net.magik6k.mpt.db.RepoBase;
import net.magik6k.mpt.util.MptFile;

import java.util.List;

/**
 * Created by marcin212 on 2015-02-19.
 */
public class RepoBrowser extends VerticalPanel {
	final int displayPos = 4;
	final int statePos = 1;
	String navRepo = "";
	String navPackage = "";
	String navFile = "";

	TextLabel nav = new TextLabel("");
	HorizontalPanel navPanel = new HorizontalPanel(2).setElementAlign(PanelAlign.MIDDLE);
	public RepoBrowser(final MptClient user) {
		super(5);
		put(new TextLabel("<hr style=\"width: 512px\"/>"), 0);
		navPanel.put(createBackButton(), 0);
		navPanel.put(nav,1);
		put(navPanel);
		put(new TextLabel("<hr style=\"width: 512px\"/>"), 3);
		put(repoList(), 4);
	}

	public Button createBackButton(){
		return new Button("Back", new ClickHandler() {
			@Override
			public void clicked() {
				if(navRepo.isEmpty()) return;
				if( !navPackage.isEmpty() && navFile.isEmpty()){
					put(getPackages(navRepo), displayPos);
					return;
				}
				if( !navFile.isEmpty() && !navPackage.isEmpty()){
					put(fileList(navPackage), displayPos);
					return;
				}
				if(!navRepo.isEmpty() && navPackage.isEmpty()){
					put(repoList(), displayPos);
				}
			}
		});
	}

	public void updateState(){
		nav.setText("<b>Currently in:</b> "+navRepo+(navPackage.isEmpty()?"":"->"+navPackage)+(navFile.isEmpty()?"":"->"+navFile));
		navPanel.put(nav, statePos);
	}

	public void setNav(String repo, String pkg, String file){
		navRepo = repo;
		navPackage = pkg;
		navFile = file;
		updateState();
	}

	public VerticalPanel repoList(){
		List<String> repos = RepoBase.instance.getAllRepos();
		VerticalPanel reposPanel = new VerticalPanel(repos.size());
		for(String s: repos){
			final String name = s;
			reposPanel.put(new InternalLink(s, new ClickHandler() {
				@Override
				public void clicked() {
					put(getPackages(name), displayPos);
				}
			}));
		}
		setNav("","","");
		return reposPanel;
	}

	public VerticalPanel getPackages(String reponame){
		List<String> packages = PackageBase.instance.getPackagesForRepo(reponame);
		VerticalPanel packagePanel = new VerticalPanel(packages.size());
		for (String s: packages) {
			final String temp = s;
			packagePanel.put(new InternalLink(s, new ClickHandler() {
				@Override
				public void clicked() {
					put(fileList(temp), displayPos);
				}
			}));
		}
		setNav(reponame,"","");
		return packagePanel;
	}

	public VerticalPanel fileList(final String packagename){
		List<MptFile> files = PackageBase.instance.getFiles(packagename);
		VerticalPanel filesPanel = new VerticalPanel(files.size());
		for (MptFile s: files) {
			final String name = s.name;
			filesPanel.put(new InternalLink(name, new ClickHandler() {
				@Override
				public void clicked() {
					put(displayFile(packagename, name), displayPos);
				}
			}));
		}
		setNav(navRepo,packagename,"");
		return filesPanel;
	}

	public VerticalPanel displayFile(String pack, String name){
		MptFile file = PackageBase.instance.getFile(pack ,name);
		VerticalPanel f = new VerticalPanel(1);
		f.put(new AceEditor(file.content, 1200, 1000,  AceMode.LUA, AceTheme.VIBRANT_INK));
		setNav(navRepo,navPackage,name);
		return f;
	}

}
