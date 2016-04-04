var roleScope = "";
var packetStatus = "";

function initEditPacket (userRoleScope, currentPacketStatus){

    roleScope = userRoleScope;
    packetStatus = currentPacketStatus;


    var validatorOptions = {excluded: [':disabled'], verbose:false};
    $('#editPacketForm').bootstrapValidator(validatorOptions);


    $("#reqApprovalBtn").on("click", function(ev){
        var packetId = $(this).attr("data-packet-id");
        var activeFromSet = $(this).attr("data-active-from-set");
        var activeFrom = $("#activeFrom").val();

        if(activeFromSet == 'true' && activeFrom)
            requestApproval(packetId);
        else
            showMessageDialog(null, "Odobrenje se ne može zatražiti dok nije zadan i spremljen datum od kada paket vrijedi!");
    });

    $("#approveBtn").on("click", function(ev){
        var packetId = $(this).attr("data-packet-id");
        approve(packetId);
    });

    $("#disapproveBtn").on("click", function(ev){
        var packetId = $(this).attr("data-packet-id");
        disapprove(packetId);
    });

    $("#returnToEditBtn").on("click", function(ev){
        var packetId = $(this).attr("data-packet-id");
        returnToEdit(packetId);
    });

    updateButtons();
}



function requestApproval(packetId){

    var url = "admin/packet/request-approval?id=" + packetId ;
    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Zahtjev za odobrenjem nije uspio!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                packetStatus = "PENDING_APPROVAL";
                updateButtons();
                showMessageDialog( null, "Zahtjev za odobrenje je uspješno poslan!");
            }
            else {
                showMessageDialog("Greška", data)
            }
        }
    });
}

function approve(packetId){

    var url = "admin/packet/approve?id=" + packetId ;
    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Odobrenje paketa  nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                packetStatus = "APPROVED";
                updateButtons();
                showMessageDialog( null, "Paket je odobren");
            }
            else {
                showMessageDialog("Greška", data)
            }
        }
    });
}

function disapprove(packetId){

    var url = "admin/packet/disapprove?id=" + packetId ;
    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Odbijanje zahtjeva za odobrenje paketa nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                packetStatus = "DISAPPROVED";
                updateButtons();
                showMessageDialog( null, "Zahtjev za odobrenje paketa je uspješno odbijen");
            }
            else {
                showMessageDialog("Greška", data)
            }
        }
    });
}

function returnToEdit(packetId){

    var url = "admin/packet/return-to-edit?id=" + packetId ;
    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Vraćanje paketa na doradu nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                packetStatus = "EDITING";
                updateButtons();
                showMessageDialog( null, "Paket je uspješno vraćen na doradu");
            }
            else {
                showMessageDialog("Greška", data)
            }
        }
    });
}

function showDeactivatePacketModal(packetId, packetName){

    $("#deactivateModal .deactivate-packet").removeClass("hide");
    $("#deactivateModal .deactivate-tariff").addClass("hide");
    $("#deactivateModal .modal-title").text("Arhiviranje paketa: " + packetName);
    $("#deactivateModal input[name='id']").val(packetId);
    $("#deactivateModal").modal("show");
}

function deactivatePacket (operatorId) {
    var data = $("#deactivateForm").serialize();
    var url = "admin/packet/deactivate";
    $.ajax(url, {
        data : data,
        error: function () {
            $("#deactivateModal").modal("hide");
            showMessageDialog("Greška", "Arhiviranje paketa nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){
            $("#deactivateModal").modal("hide");
            window.location = "admin/packet/list?operatorId=" + operatorId;
        }
    });
}



function updateButtons(){

    //if (roleScope == 'EKIP' || packetStatus != "EDITING" ) {

    if (roleScope == 'ADMIN' ) { //temporarelly disabled
        toggleFormElements("editPacketForm", true);

        $("#returnToEditBtn").prop("disabled", false);
        $("#deactivateBtn").prop("disabled", false);
        $("#reqApprovalBtn").prop("disabled", false);
        $("#approveBtn").prop("disabled", false);
        $("#disapproveBtn").prop("disabled", false);

        $("#content button:not(#reqApprovalBtn):not(#approveBtn):not(#disapproveBtn):not(#deactivateBtn):not(#returnToEditBtn)").addClass("hide");
        $("#content .btn:not(#reqApprovalBtn):not(#approveBtn):not(#disapproveBtn):not(#deactivateBtn):not(#returnToEditBtn)").addClass("hide");
    }

    //prikazi akcije nad statusom tarife ovisno o trenutnom statusu
    if (packetStatus == "EDITING") {
        $("#status").text("U pripremi");
        $("#returnToEditBtn").addClass("hide");
        $("#reqApprovalBtn").removeClass("hide");
        $("#approveBtn").addClass("hide");
        $("#disapproveBtn").addClass("hide");
    }
    else if (packetStatus == "PENDING_APPROVAL") {
        $("#status").text("Čeka na odobrenje");
        $("#disapproveBtn").text("Odbij odobrenje");
        $("#returnToEditBtn").removeClass("hide");
        $("#reqApprovalBtn").addClass("hide");
        $("#approveBtn").removeClass("hide");
        $("#disapproveBtn").removeClass("hide");
    }
    else if (packetStatus == "APPROVED") {
        $("#status").text("Odobren");
        $("#disapproveBtn").text("Poništi odobrenje");
        $("#reqApprovalBtn").addClass("hide");
        $("#approveBtn").addClass("hide");
        $("#returnToEditBtn").removeClass("hide");
        $("#disapproveBtn").removeClass("hide");
    }
    else if (packetStatus == "DISAPPROVED") {
        $("#status").text("Odobrenje odbijeno");
        $("#disapproveBtn").text("Poništi odobrenje");
        $("#reqApprovalBtn").addClass("hide");
        $("#approveBtn").removeClass("hide");
        $("#returnToEditBtn").removeClass("hide");
        $("#disapproveBtn").addClass("hide");
    }

    if(roleScope == 'OPERATOR') {
        $("#saveBtn").prop("disabled", false);

        $("#promo-on").prop("disabled", false);
        $("#promoText").prop("disabled", false);

        $("#deactivateBtn").removeClass('hide');
        $("#saveBtn").removeClass('hide');
    }

}
