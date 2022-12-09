import React from "react";
import SucceedNotification from "../../components/SuccessNotification/SucceedNotification";

export default function SignUpNotificationPage() {
  const signUpActiveContent = "successful";
  const signUpReasonContent = "Registration";
  const signUpMessage =
    "We have sent an activation link to your account to continue with the registration process.";
  return (
    <SucceedNotification
      activeContent={signUpActiveContent}
      reasonContent={signUpReasonContent}
      message={signUpMessage}
    />
  );
}
