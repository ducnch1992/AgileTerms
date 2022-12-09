import React from "react";
import FailedNotification from "../../components/FailedNotification/FailedNotification";
import { authAction } from "../../store/reducer/auth";
import { useDispatch} from "react-redux";
export default function AccessDeniedPage() {
  const dispatch = useDispatch();
  const reasonContent = "Access Denied!";
  const message = "You don't have permission to view this page";
  return <FailedNotification reasonContent={reasonContent} message={message} />;
}
