<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../../../../web.inc"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<TITLE>jplayer</TITLE>
<head>
	<link href="public/script/ui/ztesoft/jplayer/skin/blue.monday/jplayer.blue.monday.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="public/script/ui/ztesoft/jplayer/js/jquery.jplayer.min.js"></script>
	<script type="text/javascript" src="public/script/ui/ztesoft/jplayer/js/jplayer.playlist.min.js"></script>
</head>
<SCRIPT language="JavaScript">
var webroot = "<%=WebRoot%>";
//<![CDATA[
$(document).ready(function(){
	var myPlaylist = new jPlayerPlaylist({
		jPlayer: "#jquery_jplayer_1",
		cssSelectorAncestor: "#jp_container_1"
	}, [], {
		playlistOptions: {
			enableRemoveControls: true
		},
		swfPath: "public/script/ui/ztesoft/jplayer/js",
		supplied: "m4v",
		size: {
			width: "640px",
			height: "360px",
			cssClass: "jp-video-360p"
		},
		smoothPlayBar: true,
		keyEnabled: true,
		audioFullScreen: true,
		loop: true
	});
	

	$("#playBtn").click(
			function () {
				myPlaylist.setPlaylist([
            			{
            				title:"volcano.mp4",
            				m4v: webroot+"public/script/ui/ztesoft/jplayer/volcano.mp4"
            			},
            			{
            				title:"93.mp4",
            				m4v: webroot+"public/script/ui/ztesoft/jplayer/93.mp4"
            			}
            		]);
				 $("#jquery_jplayer_1").jPlayer("play");
			});
	
});



//]]>


</SCRIPT>

<BODY  >
	<div id="jp_container_1" class="jp-video jp-video-360p">
			<div class="jp-type-playlist">
				<div id="jquery_jplayer_1" class="jp-jplayer"></div>
				<div class="jp-gui">
					<div class="jp-video-play">
						<a href="javascript:;" class="jp-video-play-icon" tabindex="1">play</a>
					</div>
					<div class="jp-interface">
						<div class="jp-progress">
							<div class="jp-seek-bar">
								<div class="jp-play-bar"></div>
							</div>
						</div>
						<div class="jp-current-time"></div>
						<div class="jp-duration"></div>
						<div class="jp-controls-holder">
							<ul class="jp-controls">
								<li><a href="javascript:;" class="jp-previous" tabindex="1">previous</a></li>
								<li><a href="javascript:;" class="jp-play" tabindex="1">play</a></li>
								<li><a href="javascript:;" class="jp-pause" tabindex="1">pause</a></li>
								<li><a href="javascript:;" class="jp-next" tabindex="1">next</a></li>
								<li><a href="javascript:;" class="jp-stop" tabindex="1">stop</a></li>
								<li><a href="javascript:;" class="jp-mute" tabindex="1" title="mute">mute</a></li>
								<li><a href="javascript:;" class="jp-unmute" tabindex="1" title="unmute">unmute</a></li>
								<li><a href="javascript:;" class="jp-volume-max" tabindex="1" title="max volume">max volume</a></li>
							</ul>
							<div class="jp-volume-bar">
								<div class="jp-volume-bar-value"></div>
							</div>
							<ul class="jp-toggles">
								<li><a href="javascript:;" class="jp-full-screen" tabindex="1" title="full screen">full screen</a></li>
								<li><a href="javascript:;" class="jp-restore-screen" tabindex="1" title="restore screen">restore screen</a></li>
								<li><a href="javascript:;" class="jp-repeat" tabindex="1" title="repeat">repeat</a></li>
								<li><a href="javascript:;" class="jp-repeat-off" tabindex="1" title="repeat off">repeat off</a></li>
							</ul>
						</div>
					</div>
				</div>
				<div class="jp-playlist">
					<ul>
						<!-- The method Playlist.displayPlaylist() uses this unordered list -->
						<li></li>
					</ul>
				</div>
			</div>
		</div>
		<button id="playBtn">play</button>
</BODY>
</HTML>
