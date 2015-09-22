/**
 * fmb 插件
 * @param msg
 * @param c
 */
function octopusAlert(msg,c){
    $(".alert").html(msg);
    $(".alert").fbmodal({
         title: "温馨提示",  
         cancel: "取消",
         okay: "确定",
         okaybutton: true,
         cancelbutton: false,
         buttons: true,
         opacity: 0.0,
         fadeout: false,
         overlayclose: true,
         modaltop: "30%",
         modalwidth: "400" ,
         opacity: 0.22
     },function(callback){
    	 if(c!=null&& c!='undefined'){
    		c(callback); 
    	 }
     }); 
    return true;
}