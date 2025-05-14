async function refreshTovar(id) {
    // alert("refresh " + id);
    // return;


    let response = await fetch(`/cart/delete/${id}`,{
        method:'DELETE'
    });

    if (response.ok){
        console.log("Удален " + id);
        console.log(await response.text())
        document.querySelector(`tr[data-id='${id}']`).remove();
    }else{
        console.log("error")
    }
}
async function delTovar(id) {
    //сумма товара, который будет удален. На эту сумму нужно уменьшить сумму заказа.
    let sum = parseMoneyValue(document.querySelector(`div#q_${id}`).textContent);

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
    const cleanValue = value.replace(/\s+/g, '').replace(',', '.');
    return parseFloat(cleanValue);
}

// Функция для форматирования числа в денежный формат с двумя знаками после запятой
function formatMoneyValue(value) {
    return value.toFixed(2);
}

