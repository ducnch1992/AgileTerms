import React from 'react'
import FailedNotification from '../../components/FailedNotification/FailedNotification'


export default function AlreadyActivatedPage() {
    const reasonContent="Your account already activated"
    const message="Please login to your account"
  return <FailedNotification reasonContent={reasonContent} message={message} />;
}
