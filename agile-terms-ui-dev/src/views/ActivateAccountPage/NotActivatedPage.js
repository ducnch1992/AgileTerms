import React from "react";
import FailedNotification from "../../components/FailedNotification/FailedNotification";

export default function NotActivatedPage() {
  const reasonContent = "Account is not activated";
  const message = "Please check your mail to activated this account";
  return <FailedNotification reasonContent={reasonContent} message={message} />;
}
