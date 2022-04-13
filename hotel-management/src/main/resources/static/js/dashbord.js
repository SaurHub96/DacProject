$( document ).ready(function() {
	$("#room-dashbord").click(function(){
		dashbordLoad($("#dashbord"),"/room/roomdashbord?customerId=0");
		$("#dashbordName").text($(this).attr('data-name'));
		setTimeout(function(){
			$("#room-search").click();
		},500);
	});
	
	$("#customer-dashbord").click(function(){
		dashbordLoad($("#dashbord"),"/customer/customerdashbord");
		$("#dashbordName").text($(this).attr('data-name'));
		setTimeout(function(){
			$("#customer-search").click();
		},500);
	});
	
	$("#booking-dashbord").click(function(){
		dashbordLoad($("#dashbord"),"/booking/bookingdashbord?customerId=0");
		$("#dashbordName").text($(this).attr('data-name'));
		setTimeout(function(){
			$("#booking-search").click();
		},500);
	});
	
	$("#item-dashbord").click(function(){
		dashbordLoad($("#dashbord"),"/item");
		$("#dashbordName").text($(this).attr('data-name'));
		setTimeout(function(){
			$("#item-search").click();
		},500);
	});
	
	$("#orders-dashbord").click(function(){
		dashbordLoad($("#dashbord"),"/orders?bookingId=0");
		$("#dashbordName").text($(this).attr('data-name'));
		setTimeout(function(){
			$("#orders-search").click();
		},500);
	});
	$("#employee-dashbord").click(function(){
		dashbordLoad($("#dashbord"),"/emp/empdashbord");
		$("#dashbordName").text($(this).attr('data-name'));
		setTimeout(function(){
			$("#emp-search").click();
		},500);
	});
	
	$(document).on('click',"#add-room",function(){
		dashbordLoad($("#dashbord"),"/room?roomId=0");
	});
	$(document).on('click',"#add-customer",function(){
		dashbordLoad($("#dashbord"),"/customer?customerId=0");
	});
	$(document).on('click',"#add-item",function(){
		dashbordLoad($("#dashbord"),"/item/0");
	});
	$(document).on('click',"#add-emp",function(){
		dashbordLoad($("#dashbord"),"/emp?empId=0");
	});
	$(document).on('click',"#add-orders",function(){
		var bookingId=$("#orders-search").val();
		dashbordLoad($("#dashbord"),"/orders/"+bookingId+"/0");
		setTimeout(function(){
			$("#add-orders-form-item").click();
		},500);
		
	});
	$(document).on('click',".edit-room",function(){
		var roomId=$(this).attr("data-room-id");
		dashbordLoad($("#dashbord"),"/room?roomId="+roomId);
	});
	$(document).on('click',".edit-customer",function(){
		var customerId=$(this).attr("data-customer-id");
		dashbordLoad($("#dashbord"),"/customer?customerId="+customerId);
	});
	
	$(document).on('click',".edit-item",function(){
		var itemId=$(this).attr("data-item-id");
		dashbordLoad($("#dashbord"),"/item/"+itemId);
	});
	
	$(document).on('click',".edit-order",function(){
		var bookingId=$("#orders-search").val();
		var orderId=$(this).attr("data-order-id");
		dashbordLoad($("#dashbord"),"/orders/"+bookingId+"/"+orderId);
		setTimeout(function(){
			$("#add-orders-form-item").click();
		},500);
		
	});
	
	$(document).on('click',".edit-emp",function(){
		var empId=$(this).attr("data-emp-id");
		dashbordLoad($("#dashbord"),"/emp?empId="+empId);
	});
	
	$(document).on('click',".booking-details",function(){
		var customerId=$(this).attr("data-customer-id");
		dashbordLoad($("#dashbord"),"/booking/bookingdashbord?customerId="+customerId);
		$("#dashbordName").text("Select Customer Room");
		setTimeout(function(){
			$("#booking-search").click();
		},1000);	
	});
	
	$(document).on('click',".order-details",function(){
		var bookingId=$(this).attr("data-booking-id");
		dashbordLoad($("#dashbord"),"/orders?bookingId="+bookingId);
		$("#dashbordName").text($(this).attr('data-name'));
		setTimeout(function(){
			$("#orders-search").click();
		},500);
	});
	
	$(document).on('click',".book-customer",function(){
		var bookingCustomerId=$(this).attr('data-customer-id');
		dashbordLoad($("#dashbord"),"/room/roomdashbord?customerId="+bookingCustomerId);
		$("#dashbordName").text("Select Customer Room");
		setTimeout(function(){
			$("#room-search").click();
		},1000);	
	});
	$(document).on('click',".view-booking",function(){
		var bookingId=$(this).attr("data-booking-id");
		dashbordLoad($("#dashbord"),"/booking?type=default&bookingId="+bookingId);
	});
	$(document).on('click',".pay-booking",function(){
		var bookingId=$(this).attr("data-booking-id");
		dashbordLoad($("#dashbord"),"/booking?type=pay&bookingId="+bookingId);
	});
	
	$(document).on('click',".delete-room",function(){
		var roomId=$(this).attr("data-room-id");
		$("#delete-room-form-room-id").val(roomId);
		if(confirm("Are you sure you want delete this room.")){
			$("#delete-room-form-submit").click();	
		}
	});
	$(document).on('click',".delete-customer",function(){
		var customerId=$(this).attr("data-customer-id");
		$("#delete-customer-form-customer-id").val(customerId);
		if(confirm("Are you sure you want delete this customer.")){
			$("#delete-customer-form-submit").click();
		}
	});
	
	$(document).on('click',".delete-booking",function(){
		var bookingId=$(this).attr("data-booking-id");
		$("#delete-booking-form-booking-id").val(bookingId);
		if(confirm("Are you sure you want delete this booking.")){
			$("#delete-booking-form-submit").click();
		}
	});
	
	$(document).on('click',".delete-item",function(){
		var itemId=$(this).attr("data-item-id");
		$("#delete-item-form-item-id").val(itemId);
		if(confirm("Are you sure you want delete this Item.")){
			$("#delete-item-form-submit").click();
		}
	});
	$(document).on('click',".delete-emp",function(){
		var empId=$(this).attr("data-emp-id");
		$("#delete-emp-form-emp-id").val(empId);
		if(confirm("Are you sure you want delete this Employee.")){
			$("#delete-emp-form-submit").click();
		}
	});
	
	$(document).on('click',".delet-order",function(){
		var orderId=$(this).attr("data-order-id");
		$("#delete-orders-form-order-id").val(orderId);
		if(confirm("Are you sure you want delete this Order.")){
			$("#delete-order-form-submit").click();
		}
	});
	
	
	$(document).on('blur click change',"#room-search",function(){
		var type=$("#room-search-type").val();
		var value=$("#room-search").val();
		var bookingCustomerId=$("#booking-customerId").val();
		bookingCustomerId=bookingCustomerId==''?0:bookingCustomerId;
		if(value!=undefined && value!=''){
			dashbordLoad($("#rooms"),"/room/search?type="+type+"&value="+value+"&customerId="+bookingCustomerId);
		}else{
			dashbordLoad($("#rooms"),"/room/search?type=&value=&customerId="+bookingCustomerId);	
		}
	});
	
	$(document).on('blur click change',"#booking-search",function(){
		var type=$("#booking-search-type").val();
		var value=$("#booking-search").val();
		if(value!=undefined && value!=''){
			dashbordLoad($("#bookings"),"/booking/search?type="+type+"&value="+value);
		}else{
			dashbordLoad($("#bookings"),"/booking/search?type=&value=");	
		}
	});
	
	$(document).on('blur click change',"#customer-search",function(){
		var type=$("#customer-search-type").val();
		var value=$("#customer-search").val();
		if(value!=undefined && value!=''){
			dashbordLoad($("#customers"),"/customer/search?type="+type+"&value="+value);
		}else{
			dashbordLoad($("#customers"),"/customer/search?type=&value=");	
		}
	});
	
	$(document).on('blur click change',"#item-search",function(){
		var type=$("#item-search-type").val();
		var value=$("#item-search").val();
		if(value!=undefined && value!=''){
			dashbordLoad($("#items"),"/item/search?type="+type+"&value="+value);
		}else{
			dashbordLoad($("#items"),"/item/search?type=&value=");	
		}
	});
	
	$(document).on('blur click change',"#orders-search",function(){
		var type=$("#item-orders-type").val();
		var value=$("#orders-search").val();
		if(value!=undefined && value!=''){
			dashbordLoad($("#orders"),"/orders/search?type="+type+"&value="+value);
		}
	});
	
	$(document).on('blur click change',"#emp-search",function(){
		var type=$("#emp-search-type").val();
		var value=$("#emp-search").val();
		if(value!=undefined && value!=''){
			dashbordLoad($("#emps"),"/emp/search?type="+type+"&value="+value);
		}else{
			dashbordLoad($("#emps"),"/emp/search?type=&value=");
		}
	});
	
	
	$(document).on('click',".select-room",function(){
		$("#room-booking-div").show();
		var bookingRoomId=$(this).attr('data-room-id');
		$("#room-booking-form-customer-id").val($(this).attr('data-customer-id'));
		$("#room-booking-form-room-id").val(bookingRoomId);
		
		$("#room-booking-form-customer-name").val($("#booking-cust-name").val());
		$("#room-booking-form-room-number").val($("#roomNumber_"+bookingRoomId).text());
		$("#room-booking-form-floor-number").val($("#floorNumber_"+bookingRoomId).text());
		$("#room-booking-form-room-type").val($("#roomType_"+bookingRoomId).text());
		$("#room-booking-form-price-per-day").val($("#pricePerDay_"+bookingRoomId).text());
		$("table").hide();
		
	});
	
	$(document).on('change click',"#add-orders-form-item",function(){
		var price=$('option:selected',this).attr("data-price");
		$("#add-orders-form-item-price").val(price);
		$("#add-orders-form-item-count").click();
	});
	
	$(document).on('blur change click',"#add-orders-form-item-count",function(){
		var price=$('option:selected',"#add-orders-form-item").attr("data-price");
		var count=$(this).val();
		$("#add-orders-form-item-total").val(price*count);
	});
	$(document).on('click',"#print-pay-btn",function(){
		$("#pay-content-print").html($("#pay-slip").html());
		$("#pay-content-print").css('display','block');
	});
	$(document).on('click',"#close-pay",function(){
		$("#pay-content-print").css('display','none');
	});
	
	$(document).on('click',"#print-pay",function(){
		w=window.open();
		w.document.write($('#pay-content').html());
		w.print();
		w.close();
	});
	
	$(".nav-menu").first().click();
});
function dashbordLoad(dashbord,url){
	dashbord.load(url);
	setTimeout(function(){
		if($("#login-form").length>0){
			window.location.href="/logout";
		}
	},1000);
}