<script src="ace/ext-language_tools.js"></script>
<script>
ace.require("ace/ext/language_tools");

widgets.AceEditor.create = function(data, id){
		var element = $('<div>').height(data.height).addClass('aceCointainer')
		var editorElement = $('<div>').html(data.text).addClass('aceeditor')
		element.html(editorElement)
		var editor = ace.edit(editorElement.get(0));
		editor.setTheme(data.theme);
		editor.getSession().setMode(data.mode);

		var snd = 0;

		editor.on('input', function(){
			clearTimeout(snd);
			snd = setTimeout(function(){
				sendText(id, editor.getValue());
			},250);
		});

		editor.setOptions({
			enableBasicAutocompletion: true,
			enableSnippets: true,
			enableLiveAutocompletion: false
		});


		return {element: element, data: {editorElement: editorElement, editor: editor}}
	}
</script>