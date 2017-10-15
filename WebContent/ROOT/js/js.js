function disableButon(ids){
	var arrayId = ids.split(",");
	for (var i =0; i < arrayId.length; i++ ) {
		$("#" + arrayId[i]).attr("disabled","disabled");
	}
	return true;
}