<%@ include file="/include/i_utils.jsp" %>
<%
	int ID_ANALYSE = session.getAttribute("idAnalyse")== null ? 0 : (Integer) session.getAttribute("idAnalyse");
%>
/**
*	Js da index 
*	@autor: Mácio Matheus
*/

$(function () {
	ID_ANALYSE = <%=ID_ANALYSE %>;

	// Monitora click
	$("#btn_start").click(function () {

			
	});
});

function btClick () {
	var _url = $("#urlScan").val();

		if (_url == "") {
			alert("Informe um domínio!");
			return;
		}

		$.ajax({
			type: 'GET',
			url: "proc.jsp",
			data: {actionID: 1, url: _url},
			success: function(idAnalyse) {
				
				if (ID_ANALYSE == 0) {
					ID_ANALYSE = idAnalyse;	
				}

				if (idAnalyse > 0) {
					$(".table").show();
				} else {
					alert("Url inválida!");
				}
			}
		});
}

function getProgress() {
	
	$.ajax({
		type: 'POST',
		url: "proc.jsp",
		dataType: "JSON",
		data: {actionID: 2, idAnalyse: ID_ANALYSE},
		success: function(obj) {

			var html =  "<tr>"
	            html += 	"<td>" + obj.detailJson.qtdInputs + "</td>";
	            html += 	"<td>" + obj.detailJson.qtdForms + "</td>";
	            html += 	"<td>" + obj.detailJson.qtdResponses + "</td>";
	            html += 	"<td>" + obj.detailJson.qtdUrls + "</td>";
          		html += "</tr>";


			$("#alltrs").append(html);
		}
	});	
}