const emailChecker = (email) => {
  const regex = /^[a-zA-Z]+[0-9a-zA-Z._]*@[a-zA-Z0-9]{3,10}(\.[a-zA-Z]+){1,3}$/;

  if (regex.test(email)) {
    return true;
  } else {
    return false;
  }
};

const checkConfirmPassword = (initialPassword, confirmedPassword) => {
  return (
    confirmedPassword === initialPassword && confirmedPassword.length !== 0
  );
};

const areAllCharacterDigits = (password) => {
  let areAllDigits = true;

  for (let i = 0; i < password.length; i++) {
    if (!/\d/.test(password.charAt(i))) areAllDigits = false;
  }

  return areAllDigits;
};

const checkWhiteSpace = (str) => {
  const regex = /\s/;
  return regex.test(str);
};

const handleCheckValidPassword = (password) => {
  if (password.length < 10) {
    return false;
  } else if (checkWhiteSpace(password)) {
    return false;
  }

  let numberPoint = 0;
  let specialCharacterPoint = 0;

  if (/\d/.test(password)) {
    numberPoint = 1;
  }

  if (areAllCharacterDigits(password)) {
    return false;
  }

  let upperCase = 0;
  if (/[A-Z]/.test(password)) {
    upperCase = 1;
  }

  let lowerCase = 0;
  if (/[a-z]/.test(password)) {
    lowerCase = 1;
  }

  if (numberPoint + upperCase + lowerCase === 3) {
    return true;
  }

  const specialChars = `\`!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~`;

  specialCharacterPoint = specialChars.split("").some((specialChar) => {
    if (password.includes(specialChar)) {
      return 1;
    }
    return 0;
  });

  if (numberPoint + upperCase + lowerCase + specialCharacterPoint >= 3) {
    return true;
  }
};

const handleCheckUserName = (userName) => {
  let userNameResponse = { message: "", status: false };
  let specialCharacters = /[`!@#$%^&*()+=[\]{};':"\\|,<>/?~]/;
  let whitespace = /[ ]/;
  let userNameValid = /^[a-zA-Z0-9]+[-_.]?[a-zA-Z0-9]+/;

  if (whitespace.test(userName)) {
    userNameResponse.message = "User name contains white space";
    userNameResponse.status = false;
  } else if (specialCharacters.test(userName)) {
    userNameResponse.message =
      "User name must be alphanumeric, only one dot/hyphen/underscore in the middle of user name";
    userNameResponse.status = false;
  } else if (userName.length === 0) {
    userNameResponse.message = "User name is blank";
    userNameResponse.status = false;
  } else if (userName.length < 2) {
    userNameResponse.message = "User name must be more than 2 characters";
    userNameResponse.status = false;
  } else if (userName.length >= 39) {
    userNameResponse.message = "User name is too long (39 characters max)";
    userNameResponse.status = false;
  } else if (userNameValid.test(userName)) {
    if (userNameValid.exec(userName)[0].length !== userName.length) {
      userNameResponse.message =
        "User name must be alphanumeric, only one dot/hyphen/underscore in the middle of user name";
      userNameResponse.status = false;
    } else {
      userNameResponse.message = "Empty";
      userNameResponse.status = true;
    }
  } else if (!userNameValid.test(userName)) {
    userNameResponse.message =
      "Only support alphanumeric languages, only one dot/hyphen/underscore must be in the middle of user name";
    userNameResponse.status = false;
  }
  return userNameResponse;
};

const setOutlineStyle = (id, validatedStatus) => {
  if (validatedStatus)
    document.querySelector(`#${id}`).style.border = `2px solid #5ab75e`;
  else document.querySelector(`#${id}`).style.border = "2px solid salmon";
};

export {
  emailChecker,
  checkConfirmPassword,
  handleCheckValidPassword,
  handleCheckUserName,
  setOutlineStyle,
};
