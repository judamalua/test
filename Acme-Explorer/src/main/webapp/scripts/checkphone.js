function validate(message) {
	var f = document.getElementById("form");
	return validatePhone(f, message);
}

function validatePhone(form, message) {
	var result = true;
	var p = form["phoneNumber"].value;

	var r1 = /^(\+[0-9]{1,3} )(\([0-9]{1,3}\) )([0-9]{4,})$/g;
	var r2 = /^(\+[0-9]{1,3} )([0-9]{4,})$d/g;
	var r3 = /^([0-9]{4,})$/g;

	if (!(p === "")) {
		if (!r1.test(p)) {
			if (!r2.test(p)) {
				if (!r3.test(p)) {
					result = confirm(message);
				}
			}
		}
	}

	return result;
}

// function validateEmailAndPhone(message) {
// var f = document.getElementById("form");
// return checkEmailAndPhone(f, message);
// }
//
// function checkEmailAndPhone(form, message) {
// var result = true;
// var email = form["email"].value;
// var phoneNumber = form["phoneNumber"].value;
//
// if (email === "" && phoneNumber === "") {
// result = false;
// alert(message);
// }
//
// return result;
// }

function updateSymbol(e) {
	var selected = $(".currency-selector option:selected");
	$(".currency-symbol").text(selected.data("symbol"));
	$(".currency-amount").prop("placeholder", selected.data("placeholder"));
	$('.currency-addon-fixed').text(selected.text());
}

$(".currency-selector").on("change", updateSymbol);

updateSymbol();
