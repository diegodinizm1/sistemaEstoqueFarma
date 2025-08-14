document.addEventListener('DOMContentLoaded', function() {

    // Função para buscar as estatísticas da API
    const carregarEstatisticas = async () => {
        try {
            // Chama o endpoint de estatísticas gerais
            const responseStats = await fetch('/consultas/dashboard-stats');
            if (responseStats.redirected) window.location.href = responseStats.url;
            if (!responseStats.ok) throw new Error('Não foi possível carregar as estatísticas.');
            const stats = await responseStats.json();

            // Preenche os cards com os dados recebidos
            document.getElementById('stat-total-produtos').textContent = stats.totalProdutosCadastrados;
            document.getElementById('stat-lotes-ativos').textContent = stats.totalLotesAtivos;
            document.getElementById('stat-proximo-vencimento').textContent = stats.itensProximoVencimento;
            document.getElementById('stat-total-setores').textContent = stats.totalSetoresCadastrados;

            // Chama o endpoint para o gráfico de validade
            const responseValidade = await fetch('/consultas/estoque/validade-stats');
            if (!responseValidade.ok) throw new Error('Não foi possível carregar dados de validade.');
            const validadeData = await responseValidade.json();
            criarGraficoValidade(validadeData);

            // Chama o endpoint para o gráfico de movimentação
            const responseMovimentacao = await fetch('/consultas/movimentacoes/mensal-stats');
            if (!responseMovimentacao.ok) throw new Error('Não foi possível carregar dados de movimentação.');
            const movimentacaoData = await responseMovimentacao.json();
            criarGraficoMovimentacao(movimentacaoData);

            const responseConsumoSetor = await fetch('/consultas/consumo-por-setor');
            if (!responseConsumoSetor.ok) throw new Error('Não foi possível carregar dados de consumo por setor.');
            const consumoSetorData = await responseConsumoSetor.json();
            criarGraficoConsumoPorSetor(consumoSetorData);

        } catch (error) {
            console.error("Erro ao carregar estatísticas do dashboard:", error);
            // Coloca uma mensagem de erro nos cards
            document.getElementById('stat-total-produtos').textContent = 'Erro';
            document.getElementById('stat-lotes-ativos').textContent = 'Erro';
            document.getElementById('stat-proximo-vencimento').textContent = 'Erro';
            document.getElementById('stat-total-setores').textContent = 'Erro';
        }
    };

    // Função para criar o Gráfico de Status de Validade (Pie Chart)
    const criarGraficoValidade = (data) => {
        const ctx = document.getElementById('graficoValidade').getContext('2d');
        new Chart(ctx, {
            type: 'pie',
            data: {
                labels: ['Válidos', 'Próximos', 'Vencidos'],
                datasets: [{
                    label: '# de Lotes',
                    data: [data.lotesValidos, data.lotesProximos, data.lotesVencidos],
                    backgroundColor: ['#28a745', '#ffc107', '#dc3545'],
                    borderColor: ['#fff', '#fff', '#fff'],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                    }
                }
            }
        });
    };

    // Função para criar o Gráfico de Movimentação Mensal (Bar Chart)
    const criarGraficoMovimentacao = (data) => {
        const ctx = document.getElementById('graficoMovimentacao').getContext('2d');

        const labels = data.map(item => `${item.mes}/${item.ano}`);
        const entradas = data.map(item => item.totalEntradas);
        const saidas = data.map(item => item.totalSaidas);

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Entradas',
                    data: entradas,
                    backgroundColor: '#007bff',
                }, {
                    label: 'Saídas',
                    data: saidas,
                    backgroundColor: '#28a745',
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: {

                    },
                    y: {

                    }
                }
            }
        });
    };

    const criarGraficoConsumoPorSetor = (data) => {
        const ctx = document.getElementById('graficoConsumoPorSetor').getContext('2d');

        const labels = data.map(item => item.nomeSetor);
        const totalConsumo = data.map(item => item.totalConsumo);

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Total Consumido',
                    data: totalConsumo,
                    backgroundColor: '#17a2b8',
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }


    carregarEstatisticas();
});