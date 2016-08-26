function FlashObject(id,src,width,height)
{
	this._id = id;
	this._src = src;
	this._height = height;
	this._width = width;
	this._params = new Array();

	this.addParam = function(name,value)
	{
	}

	this.write = function(divFlash)
	{
		var strObject = '<object id="' + this._id + '" CLASSID="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="' + this._width + '" height="' + this._height + '" codebase="swflash.cab#version=6,0,29,0">';

		strObject += '<param name="movie" value="' + this._src + '">';
		strObject += '<param name="Src" value="' + this._src + '">';
		strObject += '<param name="Play" value="0">';
		strObject += '<param name="Loop" value="-1">';
		strObject += '<param   name="wmode"   value="opaque">';   
		strObject += '<param name="Quality" value="High">';
		strObject += '<param name="Scale" value="ExactFit">';
		strObject += '<embed src="'+this._src+'" width="'+this._width+'" height="'+this._height+'" quality="high"	scale="exactfit">';
		strObject += '</object>';
		divFlash.innerHTML = strObject;
	}
}
