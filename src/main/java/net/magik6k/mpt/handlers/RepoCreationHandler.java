package net.magik6k.mpt.handlers;

import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.view.RepoCreator;

public class RepoCreationHandler implements ClickHandler{

	public final MptClient user;
	
	public RepoCreationHandler(MptClient user) {
		this.user = user;
	}
	
	@Override
	public void clicked() {
		user.userPanel.put(new RepoCreator(user));
	}

}
