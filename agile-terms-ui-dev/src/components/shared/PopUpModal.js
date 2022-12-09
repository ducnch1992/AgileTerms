import React from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";

export default function PopUpModal({
  title,
  content,
  approveMessage,
  cancelMessage,
  open,
  handleClose,
  handleApproveOption,
  handleCancelOption,
}) {
  return (
      <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
        className="popup-model"
      >
        <DialogTitle id="alert-dialog-title">{title}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            {content}
          </DialogContentText>
        </DialogContent>
        <DialogActions
          sx={{
            padding: "8px 15px",
          }}
        >
          <Button
            onClick={handleApproveOption}
            className="cancelBtn"
            sx={{
              padding: "8px 15px",
              background: "#a0a0a0",
              color: "white",
              fontWeight: "bold",
              fontSize: "12px",
              "&:hover": {
                backgroundColor: "#00547c",
                color: "#fffcfc",
              },
            }}
          >
            {approveMessage}
          </Button>

          <Button
            onClick={handleCancelOption}
            className="deleteBtn"
            autoFocus
            sx={{
              padding: "8px 15px",
              background: "#fc4a1a",
              color: "white",
              fontWeight: "bold",
              fontSize: "12px",
              "&:hover": {
                backgroundColor: "#00547c",
                color: "#fffcfc",
              },
            }}
          >
            {cancelMessage}
          </Button>
        </DialogActions>
      </Dialog>
  
  );
}
