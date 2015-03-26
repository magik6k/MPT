package net.magik6k.mpt.view;

import net.magik6k.jwwf.ace.AceEditor;
import net.magik6k.jwwf.ace.AceMode;
import net.magik6k.jwwf.ace.AceTheme;
import net.magik6k.jwwf.enums.PanelAlign;
import net.magik6k.jwwf.enums.Type;
import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.input.InternalLink;
import net.magik6k.jwwf.widgets.basic.panel.Panel;
import net.magik6k.jwwf.widgets.basic.panel.TablePanel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.db.PackageBase;
import net.magik6k.mpt.db.RepoBase;
import net.magik6k.mpt.util.MptFile;

import java.util.List;

/**
 * Created by marcin212 on 2015-02-19.
 */
public class RepoBrowser extends Panel {
	final int displayPos = 2;
	final int statePos = 1;
	String navRepo = "";
	String navPackage = "";
	String navFile = "";

	TextLabel nav = new TextLabel("");
	Panel navPanel = new Panel(12, 2);
	public RepoBrowser(final MptClient user) {
		super(12, 3);
		navPanel.put(createBackButton(), 0);
		navPanel.put(nav,1);
		put(navPanel);
		put(repoList(), 2);
	}

	public Button createBackButton() {
		return new Button("Back", Type.PRIMARY, new ClickHandler() {
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

	public void updateState() {
		nav.setText("<b>Currently in:</b> "+navRepo+(navPackage.isEmpty()?"":"->"+navPackage)+(navFile.isEmpty()?"":"->"+navFile));
		navPanel.put(nav, statePos);
	}

	public void setNav(String repo, String pkg, String file){
		navRepo = repo;
		navPackage = pkg;
		navFile = file;
		updateState();
	}

	public Panel repoList() {
		List<String> repos = RepoBase.instance.getAllRepos();
		TablePanel reposPanel = new TablePanel(1, repos.size());
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
		return reposPanel.asPanel(12);
	}

	public Panel getPackages(String reponame) {
		List<String> packages = PackageBase.instance.getPackagesForRepo(reponame);
		TablePanel packagePanel = new TablePanel(1, packages.size());
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
		return packagePanel.asPanel(12);
	}

	public Panel fileList(final String packagename) {
		List<MptFile> files = PackageBase.instance.getFiles(packagename);
		TablePanel filesPanel = new TablePanel(1, files.size());
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
		return filesPanel.asPanel(12);
	}

	public Panel displayFile(String pack, String name) {
		MptFile file = PackageBase.instance.getFile(pack ,name);
		Panel f = new Panel(12, 1);
		f.put(new AceEditor(file.content, 800,  AceMode.LUA, AceTheme.VIBRANT_INK));
		setNav(navRepo,navPackage,name);
		return f;
	}

}
