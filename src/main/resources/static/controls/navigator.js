
export function writeNavigation(functionReference){
    let currentIndex = 0;
    let removeControl = document.querySelector('#navigate-control');

    if(removeControl != undefined){
        let indexer = document.querySelector('#indexer');
        currentIndex = indexer.value;
        removeControl.remove();
    }

    let navigator = document.createElement('span');
    navigator.id = "navigate-control";

    navigator.innerHTML = `<button id="navigate-control-left" class="carousel-control-prev" type="button" style="max-width: 100px;">
            <span class="carousel-control-prev-icon bg-secondary position-fixed" aria-hidden="true" onclick="navigateDirection('left', ${functionReference})"></span>
            <span class="visually-hidden">Предыдущий</span>
        </button>
        <input id="indexer" type="hidden" value="${currentIndex}"></input>
        <button id="navigate-control-right" class="carousel-control-next" type="button" style="max-width: 100px;">
            <span class="carousel-control-next-icon bg-secondary position-fixed" aria-hidden="true" onclick="navigateDirection('right', ${functionReference})"></span>
            <span class="visually-hidden">Следующий</span>
        </button>`;

    return navigator;
}
export function getCurrentIndex(){
    return document.querySelector('#indexer').value;
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
        }
        else if(direction == 'left') {
            if(currentIndex > 0){
              currentIndex--;
            }
        }
        indexer.value = currentIndex;
        return currentIndex;

}