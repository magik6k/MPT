package net.magik6k.mpt.widget;

import net.magik6k.jwwf.enums.Type;
import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.handlers.TextHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.input.InternalLink;
import net.magik6k.jwwf.widgets.basic.input.TextInput;
import net.magik6k.jwwf.widgets.basic.panel.Row;
import net.magik6k.jwwf.widgets.basic.panel.TablePanel;
import net.magik6k.jwwf.widgets.basic.panel.Panel;
import net.magik6k.mpt.db.PackageBase;
import net.magik6k.mpt.plugin.FadeOutLabel;

public class PackDependencies extends Row {
	private final String packagee;
	private final FadeOutLabel state;
	
	public PackDependencies(final String packagee) {
		super(2);
		this.packagee = packagee;
		final TextInput dependency = new TextInput("Package name");
		dependency.setTextHandler(new TextHandler() {
			@Override
			public void onType(String text) {
				if(dependency.getText().isEmpty()) {
					dependency.setStatus(Type.DEFAULT);
				} else if(PackageBase.instance.exists(dependency.getText())){
					if(!PackageBase.instance.hasDependency(packagee, dependency.getText())){
						dependency.setStatus(Type.SUCCESS);
					}else{
						dependency.setStatus(Type.WARNING);
					}
				}else{
					dependency.setStatus(Type.DANGER);
				}
			}
		});
		state = new FadeOutLabel("");
		
		this.put(new Row(3, dependency.asPanel(6), new Button("Add", new ClickHandler() {
			
			@Override
			public void clicked() {
				if(PackageBase.instance.exists(dependency.getText())){
					if(!PackageBase.instance.hasDependency(packagee, dependency.getText())){
						PackageBase.instance.addDependency(packagee, dependency.getText());
						state.setText("Dependency added");
						refreshDependencies();
					}else{
						state.setText("This dependency already exist!");
					}
				}else{
					state.setText("This package does not exist!");
				}
			}
		}).asPanel(1), state.asPanel(5)).asPanel(12));
		
		refreshDependencies();
	}
	
	private void refreshDependencies(){
		final String[] dependencies = PackageBase.instance.getDependencies(packagee);
		Panel panel = new Panel(12, 2);
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
