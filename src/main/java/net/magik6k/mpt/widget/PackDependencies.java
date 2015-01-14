package net.magik6k.mpt.widget;

import net.magik6k.jwwf.enums.PanelAlign;
import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.input.InternalLink;
import net.magik6k.jwwf.widgets.basic.input.TextInput;
import net.magik6k.jwwf.widgets.basic.panel.HorizontalPanel;
import net.magik6k.jwwf.widgets.basic.panel.TablePanel;
import net.magik6k.jwwf.widgets.basic.panel.VerticalPanel;
import net.magik6k.mpt.db.PackageBase;
import net.magik6k.mpt.plugin.FadeOutLabel;

public class PackDependencies extends VerticalPanel {
	private final String packagee;
	private final FadeOutLabel state;
	
	public PackDependencies(final String packagee) {
		super(2);
		this.packagee = packagee;
		final TextInput dependency = new TextInput("Package name");
		state = new FadeOutLabel("");
		
		this.put(new HorizontalPanel(3, dependency, new Button("Add", new ClickHandler() {
			
			@Override
			public void clicked() {
				if(PackageBase.instance.exists(dependency.getText())){
					if(!PackageBase.instance.hasDependency(packagee, dependency.getText())){
						PackageBase.instance.addDependency(packagee, dependency.getText());
						refreshDependencies();
						state.setText("Dependency added");
					}else{
						state.setText("This dependency already exist!");
					}
				}else{
					state.setText("This package does not exist!");
				}
			}
		}), state).setElementAlign(PanelAlign.MIDDLE));
		
		refreshDependencies();
	}
	
	private void refreshDependencies(){
		String[] dependencies = PackageBase.instance.getDependencies(packagee);
		VerticalPanel panel = new VerticalPanel(2);
		panel.put(new TextLabel("<b>Dependency list:</b>"));
		TablePanel deps = new TablePanel(2, dependencies.length);
		
		for(int i=0; i < dependencies.length; ++i){
			deps.put(new TextLabel(dependencies[i]));
			final int id = i;
			deps.put(new InternalLink("Delete", new ClickHandler() {
				
				@Override
				public void clicked() {
					PackageBase.instance.removeDependency(packagee, dependencies[id]);
					state.setText("Dependency removed");
					refreshDependencies();
				}
			}));
		}
		panel.put(deps);
		this.put(panel, 1);
	}
	
}
