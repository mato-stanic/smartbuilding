$(document).ready(function(){


    var validatorOptions = {excluded: [':disabled'], verbose:false};
    $('#editProfileForm').bootstrapValidator(validatorOptions);
    updateInternationalCallsMsgs();

    if(roleScope == "OPERATOR"){
        toggleFormElements("editProfileForm", true);
        $("#editProfileForm a").hide();
        $("#editProfileForm .btn").hide();

        $("#perc-home-net").prop("readonly", false);
        $("#perc-sms-home-net").prop("readonly", false);
        $("#editProfileForm .btn[type='submit']").prop("disabled", false);
        $("#editProfileForm .btn[type='submit']").show();


    }

    $("#perc-country").on("keyup", function(ev){
        calculateOtherPercent("#perc-country", "#perc-international");
    });

    $("#perc-home-net").on("keyup", function(ev){
        calculateOtherPercent("#perc-home-net", "#perc-other-net");
    });

    $("#perc-fixed").on("keyup", function(ev){
        calculateOtherPercent("#perc-fixed", "#perc-mobile");
    });

    $("#perc-local").on("keyup", function(ev){
        calculateOtherPercent("#perc-local", "#perc-national");
    });

    $("#perc-sms-country").on("keyup", function(ev){
        calculateOtherPercent("#perc-sms-country", "#perc-sms-international");
    });

    $("#perc-sms-home-net").on("keyup", function(ev){
        calculateOtherPercent("#perc-sms-home-net", "#perc-sms-other-net");
    });



});

function addCountryCodePercentage(){
    var url = "admin/user-profile/add-ccp";

    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Dodavanje nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){
            removeValidation();
            $('#international-calls-distribution table tr:last').after("<tr>" + data + "</tr>");
            setupValidation();
            updateInternationalCallsMsgs();
        }
    });
}

function calculateOtherPercent(firstFieldSelector, otherFieldSelector) {
    try {
        var countryPercent = parseFloat($(firstFieldSelector).val());
        var internationalPercent = Math.round(Math.max(100 - countryPercent, 0) * 100) / 100;;
        $(otherFieldSelector).val(internationalPercent);
    } catch (e) {
        console.log("Error: " + e);
    }
}

function deleteCountryCodePercentage(btn){

    var btnEl = $(btn);
    var ccpIndex = btnEl.attr("data-ccp-index");

    var tableRow = $(btnEl).closest("tr");
    var index = tableRow.index() - 1; //first row is header

    var url = "admin/user-profile/remove-ccp?index=" + index;

    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Brisanje nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                removeValidation();
                tableRow.remove();
                reIndexRows($("#international-calls-distribution" + " tr"), 1);
                setupValidation();
                updateInternationalCallsMsgs();
            }
            else {
                showMessageDialog("Greška", "Brisanje nije uspjelo!")
            }
        }
    });


}

function reIndexRows(rows, start){
    rows.each(function() {
        var row = $(this);
        var rowIndex = row.index() - start;
        var inputs = row.find("input, select");
        inputs.each(function(){
            var inp = $(this);
            var nameAttr = inp.attr("name");
            if(nameAttr.search(/\[(.*)\]/) > -1) {
                var newNameAttr = nameAttr.replace(/\[(.*)\]/, "[" + rowIndex + "]");
                inp.attr("name", newNameAttr);
            }
        });


        var delBtns = row.find("button");
        inputs.each(function(){
            var btn = $(this);
            btn.attr("data-ccp-index", rowIndex);
        });
    });

}


function updateInternationalCallsMsgs(){
    var count = $("#international-calls-distribution tr").length;

    if(count > 1){
        $(".other-calls-msg").text("Ostatak poziva bit će tretiran kao pozivi prema neodređenim zemljama (zona Svijet)");
        $(".no-distribution").addClass("hide");
        $("#international-calls-distribution table").removeClass("hide");
    }
    else{
        $(".other-calls-msg").text("Svi poziv bit će tretirani kao pozivi prema neodređenim zemljama (zona Svijet)");
        $(".no-distribution").removeClass("hide");
        $("#international-calls-distribution table").addClass("hide");
    }

}

function removeValidation() {
    $('#editProfileForm').data('bootstrapValidator').destroy();
    $('#editProfileForm').data('bootstrapValidator', null);
}

function setupValidation() {
    $('#editProfileForm').bootstrapValidator({excluded: [':disabled'], verbose:false});
}
