function validate(message) {
	var f = document.getElementById("form");
	return validatePhone(f, message);
}

function validatePhone(form, message) {
	var result = true;
	var p = form["phoneNumber"].value;

	var r1 = /^(\+[0-9]{1,3} )(\([0-9]{1,3}\) )([0-9]{4,})$/g;
	var r2 = /^(\([0-9]{1,3}\) )([0,9]{4,})$/g;
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
