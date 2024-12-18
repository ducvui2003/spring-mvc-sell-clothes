export class swal2Icon {
    static SUCCESS = "success";
    static ERROR = "error";
    static WARNING = "warning";
    static INFO = "info";
    static QUESTION = "question";
}


export function alert(mes, type) {
    Swal.fire({
        position: 'top',
        title: 'THÔNG BÁO',
        html: mes,
        icon: type != null ? type : 'success',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        // showCancelButton: false,
        // confirmButtonText: 'Yes!'
    })
}

export function successAlert(mes) {
    alert(mes, 'success');
}

export function errorAlert(mes = "Hệ thống đang bận, vui lòng thử lại sau") {
    alert(mes, 'error');
}

export function warningAlert(mes) {
    alert(mes, 'warning');
}

export function infoAlert(mes) {
    alert(mes, 'info');
}

export function questionAlert(mes) {
    alert(mes, 'question');
}

export function autoCloseAlert(mes, time = 1500, url = null) {
    Swal.fire({
        position: 'top',
        title: 'THÔNG BÁO',
        html: mes,
        icon: 'info',
        // showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        showCancelButton: false,
        confirmButtonText: 'CLOSE',
        timer: time,
        timerProgressBar: true,
        didOpen: () => {
            Swal.showLoading();
            timerInterval = setInterval(() => {
                const content = Swal.getHtmlContainer();
                if (content) {
                    const b = content.querySelector('b');
                    if (b) {
                        b.textContent = Swal.getTimerLeft();
                    }
                }
            }, 100);
        },
        willClose: () => {
            clearInterval(timerInterval);
        }
    }).then((result) => {
        if (url != null) location.href = url;
    })
}

export function autoCloseAlertWithFunction(mes, time = 1500, icon = swal2Icon.INFO, Function = (result) => {
}) {
    Swal.fire({
        position: 'top',
        title: 'THÔNG BÁO',
        html: mes,
        icon: icon,
        // showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        showCancelButton: false,
        confirmButtonText: 'CLOSE',
        timer: time,
        timerProgressBar: true,
        didOpen: () => {
            Swal.showLoading();
            const timer = Swal.getPopup().querySelector("b");
            timerInterval = setInterval(() => {
                timer.textContent = `${Swal.getTimerLeft()}`;
            }, 1000);
        },
        willClose: () => {
            clearInterval(time);
        }
    }).then((result) => {
        Function(result);
    })
}

export function flashCloseAlert(time = 1500, icon = 'info') {
    Swal.fire({
        position: 'top',
        icon: icon,
        width: 250,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        showCancelButton: false,
        confirmButtonText: 'CLOSE',
        showConfirmButton: false,
        timer: time,
        willClose: () => {
            clearInterval(time);
        }
    });
}

export function autoCloseAlertIcon(mes, time = 1500, icon = 'info', url = null) {
    console.log(mes)
    Swal.fire({
        position: 'top',
        title: 'THÔNG BÁO',
        html: mes,
        icon: icon,
        // showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        showCancelButton: false,
        confirmButtonText: 'CLOSE',
        timer: time,
        timerProgressBar: true,
        didOpen: () => {
            Swal.showLoading()
            timerInterval = setInterval(() => {
                const content = Swal.getContent()
                if (content) {
                    const b = content.querySelector('b')
                    if (b) {
                        b.textContent = Swal.getTimerLeft()
                    }
                }
            }, 100)
        },
        willClose: () => {
            clearInterval(time);
        }
    }).then((result) => {
        // console.log( url);
        if (url != null) location.href = url;
    })
}