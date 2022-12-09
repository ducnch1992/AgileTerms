import React from "react";

export default function PasswordLabel({ isPasswordValid }) {
  return (
    <>
      {!isPasswordValid && (
        <div className="badge text-wrap password-suggest font-italic container-fluid">
          The password must: <br />
          Have at least 10 characters and doesn't contain whitespace <br />
          Meet 3 or 4 of these following requirements: <br />
          Includes at least 1 upper case <br />
          Includes at least 1 lower case <br />
          Includes at least 1 number <br />
          Includes at least 1 special character <br />
        </div>
      )}
    </>
  );
}
