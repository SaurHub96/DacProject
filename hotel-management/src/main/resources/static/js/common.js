function getFormData(formId){
    var unindexed_array = $("#"+formId).serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}
$( document ).ready(function() {
   $("#signUpForm").click(function (){
		$("#login-form").hide();
		$("#signup-form").show();
	});
	
	$("#loingForm").click(function (){
		$("#signup-form").hide();
		$("#login-form").show();
	});
	
	$("#about").click(function(){
		dashbordLoad($("#dashbord"),"/about");
		$("#dashbordName").text($(this).attr('data-name'));
	});
	
	$("#contact").click(function(){
		dashbordLoad($("#dashbord"),"/contact");
		$("#dashbordName").text($(this).attr('data-name'));
	});
	
	$("#signin").click(function(){
		location.reload();
	});
	
	$(document).on('click',".submitBtn",function(){
		var btnId=this.id;
		var formId=btnId.replace("-submit","");
		var formData=getFormData(formId);
		if(formId=='add-emp-form'){
			var reception={};
			reception.password=$("#add-emp-form-password").val();
			if(reception.password!=''){
				formData.reception=reception;
			}
		}
		var actionURL=$("#"+formId).attr("data-url");
		var method=$("#"+formId).attr("method");
		$.ajax({
                url: actionURL, 
                type : method, 
                contentType: 'application/json',
                dataType : 'json', 
                data :JSON.stringify(formData),
                success : function(result) {
					if(result.success!=undefined){
						successAction(formId,result);
					}else{
						errorAction(result);
					}
				},
				error : function(xhr, resp, text) {
					if(typeof resp == "object"){
						alert(JSON.stringify(resp) + "\n" + text);
					}else{
						alert(resp);
					}
					location.reload();
				}
		});
				
		
	});
	
});


function successAction(formId,result){
	alert(result.success);
	switch(formId){
		case "delete-room-form":{
			$("#room-search").click();
			break;	
		}
		case "delete-customer-form":{
			$("#customer-search").click();
			break;	
		}
		case "update-booking-form":{
			$("#booking-dashbord").click();
			break;	
		}
		case "delete-booking-form":{
			$("#booking-search").click();
			break;	
		}
		default:{
			location.reload();		
		}
	}
}

function errorAction(result){
	if(result.validation!=undefined){
		var validationMessae="Validation Failed for \n";
		var i=1;
		result.validation.forEach(function(e){
			validationMessae+=i+".  "+e+"\n";
			i++;
		});
		alert(validationMessae);
	}else{
		if(result.error!=undefined){
			alert(result.error);
		}else{
			alert("There is some problem, Please try later");
		}
		location.reload();	
	}
	
}

$(document).on('blur',"input",function(){
	$(this).val($(this).val().trim());
});


function dashbordLoad(dashbord,url){
	dashbord.load(url);
	setTimeout(function(){
		if($("#login-form").length>0){
			window.location.href="/logout";
		}
	},1000);
}
