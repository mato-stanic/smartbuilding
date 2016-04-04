var confirmCallback = function(){
    alert("Confirm callback function not implemented!");
}

function showConfirmDialog(onConfirmFn, title, message, okBtnText, cancelBtnText){

    confirmCallback = onConfirmFn;

    $("#confirmDeleteModal .modal-title").html(title ? title : "Potvrdi");
    $("#confirmDeleteModal .modal-body").html(message ? message : "Jeste li sigurni da želite izvršiti ovu akciju?");
    $("#confirmDeleteModal .ok-btn").html(okBtnText ? okBtnText : "Potvrdi");
    $("#confirmDeleteModal .cancel-btn").html(cancelBtnText ? cancelBtnText : "Otkaži");

    if(title)
        $("#confirmDeleteModal .modal-header").show();
    else
        $("#confirmDeleteModal .modal-header").hide();


    $("#confirmDeleteModal").modal("show");
}

function hideConfirmDialog(){
    $("#confirmDeleteModal").modal("hide");
}

function showMessageDialog(title, message){
    $("#messageModal .modal-title").html(title ? title : "Poruka");
    $("#messageModal .modal-body").html(message ? message : "");
    if(title)
        $("#messageModal .modal-header").show();
    else
        $("#messageModal .modal-header").hide();

    $("#messageModal").modal("show");
}

function hideConfirmDialog(){
    $("#messageModal").modal("hide");
}


function toggleFormElements(formName, bDisabled) {
    $("#" + formName + " input").prop("readonly", bDisabled);
    $("#" + formName + " select").prop("disabled", bDisabled);
    $("#" + formName + " textarea").prop("readonly", bDisabled);
    $("#" + formName + " button").prop("disabled", bDisabled);
}

function toggleTreeNodeExpand(linkNode){
    var nodeEl = $(linkNode).find("i");

    if(nodeEl.hasClass("glyphicon-collapse-down")){
        nodeEl.removeClass("glyphicon-collapse-down");
        nodeEl.addClass("glyphicon-collapse-up");
        $(linkNode).attr("title", "Sakrij detaljniju raspodjelu");

    }
    else {
        nodeEl.removeClass("glyphicon-collapse-up");
        nodeEl.addClass("glyphicon-collapse-down");
        $(linkNode).attr("title", "Prikaži detaljniju raspodjelu");

    }

}

function showTermsDialog(){
    $("#termsModal").modal("show");
}



function acceptTerms(){
    var url = "accept-terms";
    $.post( url, function( data ) {
        if (data == "SUCCESS")
            $("#termsModal").modal("hide");
    });
}