export default function getTextColorFromBackground(bgColor) {
  const TEXT_LIGHT_COLOR = "#FFFFFF";
  const TEXT_DARK_COLOR = "#424242";
  let color = "";
  if (bgColor) {
    color = bgColor.charAt(0) === "#" ? bgColor.substring(1, 7) : bgColor;
  }
  let r = parseInt(color.substring(0, 2), 16);
  let g = parseInt(color.substring(2, 4), 16);
  let b = parseInt(color.substring(4, 6), 16);
  return r * 0.299 + g * 0.587 + b * 0.114 > 149
    ? TEXT_DARK_COLOR
    : TEXT_LIGHT_COLOR;
}
