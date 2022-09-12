
export function writeNavigation(functionReference){

let removeControl = document.querySelector('#navigate-control');
if(removeControl != undefined){
    removeControl.remove();
}

let navigator = document.createElement('span');
navigator.id = "navigate-control";

navigator.innerHTML = `<button id="navigate-control-left" class="carousel-control-prev" type="button" style="max-width: 100px;">
            <span class="carousel-control-prev-icon bg-secondary position-fixed" aria-hidden="true" onclick="navigateDirection('left', ${functionReference})"></span>
            <span class="visually-hidden">Предыдущий</span>
        </button>
        <input id="indexer" type="hidden" value="0"></input>
        <button id="navigate-control-right" class="carousel-control-next" type="button" style="max-width: 100px;">
            <span class="carousel-control-next-icon bg-secondary position-fixed" aria-hidden="true" onclick="navigateDirection('right', ${functionReference})"></span>
            <span class="visually-hidden">Следующий</span>
        </button>`;

return navigator;
}

window.navigateDirection = function navigateDirection(direction, functionReference){
        let index = calculateIndex(direction);
        functionReference(index);

}
function calculateIndex(direction){
        let indexer = document.querySelector('#indexer');
        let currentIndex = indexer.value;
        let maxCount = 100;

        if(direction == 'right'){
            currentIndex++;
            if(currentIndex >= maxCount){
              currentIndex = maxCount;
            }
        }
        else if(direction == 'left') {
            if(currentIndex > 0){
              currentIndex--;
            }
        }
        indexer.value = currentIndex;
        return currentIndex;

}