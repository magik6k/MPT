package net.magik6k.mpt.plugin;

import net.magik6k.jwwf.core.JwwfPlugin;
import net.magik6k.jwwf.core.JwwfServer;
import net.magik6k.jwwf.core.plugin.IPluginGlobal;

public class MptPlugin extends JwwfPlugin implements IPluginGlobal {

	@Override
	public void onAttach(JwwfServer server) {
		server.getCreator().registerWidget("FadeOutLabel", 
				"	var elem = $('<span>').css('white-space','nowrap').addClass('jwwfElement').html(data.text).delay(250).fadeOut(500);"
				+ "	return {element:elem, data:{}}\n"
				, "	widget.element.html(data.text).stop().show().delay(250).fadeOut(500);");
		
		server.getCreator().appendHead("<style>.jwwfA {white-space: nowrap;}</style>");
	}

}
