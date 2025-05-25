//сортировка таблицы по указанному столбцу
let sortStates = {};

function sortTable(columIndex) {
    let body = document.getElementsByTagName("tbody")[0];
    let headers = document.getElementsByTagName("th");
    let rows = Array.from(body.getElementsByTagName("tr"));

    if (sortStates[columIndex] === undefined){
        sortStates[columIndex] = 'asc';
    }

    let sortedRows = rows.sort((rowA , rowB) => {
            cellA = rowA.cells[columIndex].innerText.toLowerCase();
            cellB = rowB.cells[columIndex].innerText.toLowerCase();

        let sortMulti = sortStates[columIndex] === 'asc' ? 1 : -1;

        if (!isNaN(cellA) && !isNaN(cellB)){
            // return sortMulti * (Number(cellA) - Number(cellB));
            return sortMulti * (parseInt(cellA) - parseInt(cellB));
        }

        return sortMulti * cellA.localeCompare(cellB);
    });

    sortedRows.forEach(row=>{
        body.appendChild(row);
    });

    //Name ⬆
    for (const header of headers) {
        let arr = header.innerText.split(" ");
        header.innerText = arr[0] ;
    }

    if (sortStates[columIndex] === 'asc'){
        // console.log('asc')
        sortStates[columIndex] = 'desc'

        headers[columIndex].innerHTML = headers[columIndex].innerText.trim() + '&nbsp;<i class="bi bi-arrow-up-square"></i>'
    }else{
        // console.log('desc')
        sortStates[columIndex] = 'asc'
        headers[columIndex].innerHTML = headers[columIndex].innerText.trim() + '&nbsp;<i class="bi bi-arrow-down-square"></i>'
    }

}

//генерация чекбокса для динамических форм
function drawCB(id,status) {
    let checked = status == 0 ? 'checked="checked"' : '';
    return `<div className="form-check">
            <input className="form-check-input" type="checkbox" id="blok_${id}" onClick="setBlokStatus('blok_${id}')"
                   ${checked}"/>
        </div>`;
}

function doSearch(formId,field){
    let form = document.getElementById(formId);
    let searchText = document.getElementById("text-to-search").value.trim();
    let searchField = document.getElementById("field-to-search");

    if (searchText.length < 2) {
        alert('Введите минимум 2 символа для поиска');
        return;
    }

    if (field === 'tovName' && searchText.length <= 3) {
        alert('Для поиска по наименованию введите минимум 4 символа');
        return;
    }
    searchField.value = field;
    console.log("Search text = " + searchText);
    console.log("Search field = " + searchField.value);
    console.log("Form action = " + form.getAttribute('action'));
    form.submit();

}