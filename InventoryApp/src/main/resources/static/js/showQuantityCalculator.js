let divCalculator = document.getElementById("divCalculator");

const showQuantityCalculator = () => {
  if (
    divCalculator.style.display === "none" ||
    divCalculator.style.display === ""
  ) {
    divCalculator.style.display = "block";
  } else {
    divCalculator.style.display = "none";
  }

  console.log("click");
};
