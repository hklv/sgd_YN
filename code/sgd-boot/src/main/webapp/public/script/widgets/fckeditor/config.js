/**
 * @license Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
//	config.toolbarGroups = [
//	                		{ name: 'clipboard',   groups: [ 'clipboard', 'undo' ] },
//	                		{ name: 'editing',     groups: [ 'find', 'selection', 'spellchecker' ] },
//	                		{ name: 'links' },
//	                		{ name: 'insert' },
//	                		{ name: 'forms' },
//	                		{ name: 'tools' },
//	                		{ name: 'document',	   groups: [ 'mode', 'document', 'doctools' ] },
//	                		{ name: 'others' },
//	                		'/',
//	                		{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
//	                		{ name: 'paragraph',   groups: [ 'list', 'indent', 'blocks', 'align', 'bidi' ] },
//	                		{ name: 'styles' },
//	                		{ name: 'colors' },
//	                		{ name: 'about' }
//	                	];
//	
	config.toolbarGroups = [
	                		{ name: 'clipboard',   groups: [ 'clipboard', 'undo' ] },
	                		{ name: 'links' },
	                		{ name: 'insert' },
	                		//{ name: 'forms' },
	                		{ name: 'tools' },
	                		{ name: 'document',	   groups: [ 'mode', 'document' ] },
	                		{ name: 'basicstyles', groups: [ 'basicstyles' ] },
	                		{ name: 'paragraph',   groups: [ 'list', 'indent','align'] },
	                		{ name: 'styles' },
	                		{ name: 'colors' }
	                		//{ name: 'about' }
	                	];
	
	config.removeButtons = 'Subscript,Superscript,Flash,Iframe,Print,Save,ShowBlocks,NewPage,Anchor,Smiley,Styles,PageBreak,Indent,SpecialChar';
	
	config.language = 'zh-cn';
	config.enterMode = CKEDITOR.ENTER_BR;
	config.filebrowserWindowWidth= '700';
	config.filebrowserWindowHeight= '500';
	config.filebrowserWindowFeatures = 'resizable=no,scrollbars=no,location=no';

	// Se the most common block elements.
	config.format_tags = 'p;h1;h2;h3;pre';

	// Make dialogs simpler.
	config.removeDialogTabs = 'image:advanced;link:advanced';
};
