import React, { useState } from "react";

const DescriptionContent = ({ content }) => {
  const [fullLength, setFullLength] = useState(false);
  let isMobile = false;
  if (window.innerWidth <= 576) {
    isMobile = true;
  }
  const showText = () => {
    if (!isMobile || (isMobile && (content.length <= 150))) {
      return content;
    } else {
      if (!fullLength) {
        return (
          <>
            {content.slice(0, 150) + "..."}
            <span className="text-primary" onClick={() => setFullLength(true)}>
              <> Show more</>
            </span>
          </>
        );
      } else
        return (
          <>
            {content}
            <span className="text-primary" onClick={() => setFullLength(false)}>
              <> Show less</>
            </span>
          </>
        );
    }
  };



  
  return (
    <>
      {showText()}
      {/* {(fullLength || !isMobile) && content}
      {!fullLength && isMobile && content.slice(0, 10) + "..."}
      {isMobile && (
        <span
          className="text-primary"
          onClick={() => setFullLength(!fullLength)}
        >
          {fullLength && isMobile ? <> Show less</> : <> Show more</>}
        </span>
      )} */}
    </>
  );
};

export default DescriptionContent;
