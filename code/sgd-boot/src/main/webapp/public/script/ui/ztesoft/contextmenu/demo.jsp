<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../../../../web.inc"%>
<HTML>
 <HEAD>
  <TITLE> JQuery右键菜单 </TITLE>
<script type="text/javascript" src="<%=WebRoot%>public/script/ui/ztesoft/contextmenu/jquery.contextmenu.r2.js"></script>
 </HEAD>

 <BODY>
 <span class="demo1" style="color:green;">
    右键点此
 </span>
<hr />
<div id="demo2">
    右键点此
</div>
<hr />
<div class="demo3" id="dontShow">
  不显示
</div>
<hr />
<div class="demo3" id="showOne">
  显示第一项
</div>
<hr />
<div class="demo3" id="showAll">
  显示全部
</div>

<hr />
    <!--右键菜单的源-->
     <div class="contextMenu" id="myMenu1">
      <ul>
        <li id="open"> 打开</li>
        <li id="email"> 邮件</li>
        <li id="save"> 保存</li>
        <li id="delete">关闭</li>
      </ul>
    </div>

    <div class="contextMenu" id="myMenu2">
        <ul>
          <li id="item_1">选项一</li>
          <li id="item_2">选项二</li>
          <li id="item_3">选项三</li>
          <li id="item_4">选项四</li>
        </ul>
   </div>
    
     <div class="contextMenu" id="myMenu3">
         <ul>
          <li id="item_1">csdn</li>
          <li id="item_2">javaeye</li>
          <li id="item_3">itpub</li>
        </ul>
    </div>
 </BODY>
 <script>
    //所有class为demo1的span标签都会绑定此右键菜单
     $('span.demo1').contextMenu('myMenu1', 
     {
          bindings: 
          {
            'open': function(t) {
              alert('Trigger was '+t.id+'\nAction was Open');
            },
            'email': function(t) {
              alert('Trigger was '+t.id+'\nAction was Email');
            },
            'save': function(t) {
              alert('Trigger was '+t.id+'\nAction was Save');
            },
            'delete': function(t) {
              alert('Trigger was '+t.id+'\nAction was Delete');
            }
          }

    });
    //所有html元素id为demo2的绑定此右键菜单
    $('#demo2').contextMenu('myMenu2', {
      //菜单样式
      menuStyle: {
        border: '2px solid #000'
      },
      //菜单项样式
      itemStyle: {
        fontFamily : 'verdana',
        backgroundColor : 'green',
        color: 'white',
        border: 'none',
        padding: '1px'

      },
      //菜单项鼠标放在上面样式
      itemHoverStyle: {
        color: 'blue',
        backgroundColor: 'red',
        border: 'none'
      },
      //事件    
      bindings: 
          {
            'item_1': function(t) {
              alert('Trigger was '+t.id+'\nAction was item_1');
            },
            'item_2': function(t) {
              alert('Trigger was '+t.id+'\nAction was item_2');
            },
            'item_3': function(t) {
              alert('Trigger was '+t.id+'\nAction was item_3');
            },
            'item_4': function(t) {
              alert('Trigger was '+t.id+'\nAction was item_4');
            }
          }
    });
    //所有div标签class为demo3的绑定此右键菜单
    $('div.demo3').contextMenu('myMenu3', {
    //重写onContextMenu和onShowMenu事件
      onContextMenu: function(e) {
        if ($(e.target).attr('id') == 'dontShow') return false;
        else return true;
      },

      onShowMenu: function(e, menu) {
        if ($(e.target).attr('id') == 'showOne') {
          $('#item_2, #item_3', menu).remove();
        }
        return menu;
      }

    });



 </script>
</HTML>