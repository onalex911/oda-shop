let form = document.getElementById('do-sort');
form.addEventListener('change', function (event) {
    if (event.target.classList.contains('select-box')) {
        let changedEl = event.target;
        let sourceURL = document.getElementById("src-path").value;
        let sort_by = document.getElementById("sort-by").value;
        let pg_size = document.querySelector('select[name="size"]').value;

        // Обновляем URL в зависимости от того, какой селектор изменился
        let url = `${sourceURL}sort=${sort_by}&size=${pg_size}&page=0`;
        console.log("Redirecting to URL:" + url);

        // Перенаправляем на новый URL
        window.location.href = url;
    }
});

async function addTovar(id) {
    let quantity = parseInt(document.getElementById(`q_${id}`).value);
    let price = parseMoneyValue(document.getElementById(`p_${id}`).textContent);

    // alert(`Adding /cart/add/${id}/${quantity}`);
    let response = await fetch(`/cart/add/${id}/${quantity}`,{
        method:'POST'
    });

    if (response.ok){
        let result = await response.json();
        // alert(result.text())

        document.getElementsByClassName('item-counter')[0].innerText = result.amountPos;
        document.getElementsByClassName('item-price')[0].innerText = formatMoneyValue(result.totalSum);
        let button = document.getElementById(`cart-${id}`);
        if(button !== 'undefined'){
            button.classList.add('active');
            button.onclick = () => goToCart();
            button.title = "Перейти к заказу";
            document.getElementById(`ion-${id}`).classList.replace('ion-md-cart','ion-md-checkmark-circle');
        }
    }else{
        console.log("error")
    }
}

async function refreshTovar(id,price) {
    let newQuantity = parseInt(document.getElementById(`q_${id}`).value);

    let response = await fetch(`/cart/refresh/${id}/${newQuantity}`,{
        method:'POST'
    });

    if (response.ok){
        // alert("Обновлен " + id);
        console.log(await response.text())
        document.querySelector(`#s_${id}`).textContent = formatMoneyValue(newQuantity * parseMoneyValue(price));
    }else{
        console.log("error")
    }
}
async function delTovar(id) {
    //сумма товара, который будет удален. На эту сумму нужно уменьшить сумму заказа.
    let sum = parseMoneyValue(document.getElementById(`s_${id}`).textContent);

    let response = await fetch(`/cart/remove/${id}`,{
        method:'DELETE'
    });

    if (response.ok){
        // console.log(`Удален ${id} на сумму ${sum}`);
        console.log(await response.text())
        //удаляем строку товара в таблице Корзины
        document.querySelector(`tr[data-id='${id}']`).remove();
        //уменьшаем кол-во позиций в Корзине
        let amount = document.querySelector('.item-counter');
        amount.innerText = parseInt(amount.textContent) - 1;
        //получаем общую сумму
        let totalSumEl = document.querySelector('.item-price');
        let totalSum = parseMoneyValue(totalSumEl.textContent);

        // Проверяем, что оба значения - числа
        if (!isNaN(totalSum) && !isNaN(sum)) {
            //корректируем общую сумму
            totlalSum.textContent = formatMoneyValue(totalSum - sum);
        } else {
            console.error('Один из элементов содержит нечисловое значение');
        }
    }else{
        console.log("error")
    }
}

// Функция для преобразования строки в денежное значение (учитывает возможные разделители)
function parseMoneyValue(value) {
    // Удаляем все пробелы, заменяем запятые на точки
    const cleanValue = value.replace(/[а-яА-Я\s\.]+/g, '').replace(',', '.');
    return parseFloat(cleanValue);
}

// Функция для форматирования числа в денежный формат с двумя знаками после запятой
function formatMoneyValue(value) {
    return value.toFixed(2);
}

function goToCart(){
    window.location.href = '/cart';
}

function goBackAndReload() {
    window.history.back();
    setTimeout(() => {
        location.reload();
    }, 100); // небольшая задержка для гарантии возврата
}
