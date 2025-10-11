import {formatCurrency, http} from "../base.js";

$(document).ready(function () {
    const monthInput = $('#month');
    const yearInput = $('#year');
    const formFilter = $("#form-filter");
    const revenue = $('#revenue');
    const orderSuccess = $('#order-success');
    const orderFailed = $('#order-failed');
    let productsPopularChart, productsNotPopularChart
    const current = new Date();
    init();

    function init() {
        initMonthAndYear();
        initChart();
        handleChartUpdate(current.getMonth() + 1, current.getFullYear());
        handleFormFilter();
    }

    function handleFormFilter() {
        formFilter.on('submit', function (e) {
            e.preventDefault();
            const month = monthInput.val();
            const year = yearInput.val();
            if (month && year) {
                handleChartUpdate(month, year);
                return;
            }
            Swal.fire({
                icon: 'warning',
                title: 'Vui lòng chọn tháng và năm',
            });
        });
    }

    function initMonthAndYear() {
        const months = [
            {value: 1, text: "Tháng 1"},
            {value: 2, text: "Tháng 2"},
            {value: 3, text: "Tháng 3"},
            {value: 4, text: "Tháng 4"},
            {value: 5, text: "Tháng 5"},
            {value: 6, text: "Tháng 6"},
            {value: 7, text: "Tháng 7"},
            {value: 8, text: "Tháng 8"},
            {value: 9, text: "Tháng 9"},
            {value: 10, text: "Tháng 10"},
            {value: 11, text: "Tháng 11"},
            {value: 12, text: "Tháng 12"},
        ];

        // Populate month dropdown
        const configSelect2 = {
            width: '100%',
            closeOnSelect: true,
            allowClear: true,
            language: 'vi',
            data: [],
        };
        monthInput.select2({
            ...configSelect2,
            placeholder: 'Chọn tháng',
        })
        yearInput.select2({
            ...configSelect2,
            placeholder: 'Chọn năm',
        })
        months.forEach((month) => {
            let option = new Option(month.text, month.value);
            if (month.value === current.getMonth() + 1)
                option.setAttribute('selected', 'selected');
            monthInput.append(option);
        });

        const currentYear = new Date().getFullYear();
        for (let year = currentYear; year >= 2014; year--) {
            let option = new Option("Năm " + year, year);
            if (year === current.getFullYear())
                option.setAttribute('selected', 'selected');
            yearInput.append(option);
        }
    }

    function initChart() {
        // Chart.js configuration
        const ctx = document.getElementById('product-popular-charts').getContext('2d');
        const baseConfig = {
            type: 'bar',
            data: {
                labels: [],
                datasets: [{
                    label: 'Số lượng',
                    data: [],
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                plugins: {
                    title: {
                        display: true,
                        text: '5 sản phẩm bán chạy nhất (phổ biến)',
                        font: {
                            size: 24,
                            weight: 'bold'
                        },
                        padding: {
                            top: 30,
                            bottom: 20
                        }
                    }
                },
                scales: {
                    x: {
                        display: true,
                        title: {
                            display: true,
                            text: 'Sản phẩm',
                            color: '#911',
                            font: {
                                size: 18,
                            },
                            padding: {top: 10, left: 0, right: 0, bottom: 0}
                        },
                        ticks: {
                            autoSkip: false, // Ensure no labels are skipped
                            maxRotation: 0,
                            minRotation: 0,
                            font: {
                                size: 17,
                                lineHeight: 1.67,
                            }
                        }
                    },
                    y: {
                        beginAtZero: true,
                        display: true,
                        title: {
                            display: true,
                            text: 'Số luợng bán',
                            color: '#911',
                            font: {
                                size: 18,
                            },
                            padding: {top: 0, left: 0, right: 0, bottom: 20}
                        },
                        ticks: {
                            stepSize: 2,
                        },
                    }
                }
            }
        }

        productsPopularChart = new Chart(ctx, {...baseConfig,});

        productsNotPopularChart = new Chart(document.getElementById('product-not-popular-charts').getContext('2d'), {
            ...baseConfig,
            options: {
                ...baseConfig.options,
                plugins: {
                    ...baseConfig.options.plugins,
                    title: {
                        ...baseConfig.options.plugins.title,
                        text: '5 sản phẩm ít bán chạy nhất'
                    }
                }
            }
        });

    }

    function handleChartUpdate(month, year) {
        http({
            url: "/api/admin/dashboard/detail",
            method: "GET",
            data: {
                month: month,
                year: year,
            },
        }).then(function (response) {
            revenue.text(formatCurrency((response.result.revenue || "0")));
            orderSuccess.text((response.result.orderSuccess || "0") + " đơn hàng");
            orderFailed.text((response.result.orderFailed || "0") + " đơn hàng");
            {
                const labels = response.result.popular.map(product => {
                    const words = product.name.split(" ");
                    const mergedWords = [];
                    for (let i = 0; i < words.length; i += 3) {
                        let merged = words[i];
                        if (i + 1 < words.length) {
                            merged += " " + words[i + 1];
                        }
                        if (i + 2 < words.length) {
                            merged += " " + words[i + 2];
                        }
                        mergedWords.push(merged);
                    }
                    return mergedWords;
                })
                const data = response.result.popular.map(product => product.quantity);
                productsPopularChart.data.labels = labels;
                productsPopularChart.data.datasets[0].data = data;
                productsPopularChart.update();
            }
            {
                const labels = response.result.notPopular.map(product => {
                    const words = product.name.split(" ");
                    const mergedWords = [];
                    for (let i = 0; i < words.length; i += 3) {
                        let merged = words[i];
                        if (i + 1 < words.length) {
                            merged += " " + words[i + 1];
                        }
                        if (i + 2 < words.length) {
                            merged += " " + words[i + 2];
                        }
                        mergedWords.push(merged);
                    }
                    return mergedWords;
                })
                const data = response.result.notPopular.map(product => product.quantity);
                productsNotPopularChart.data.labels = labels;
                productsNotPopularChart.data.datasets[0].data = data;
                productsNotPopularChart.update();
            }
        });
    }
});