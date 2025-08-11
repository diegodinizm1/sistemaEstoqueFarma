document.addEventListener('DOMContentLoaded', function() {

    // Função para buscar as estatísticas da API
    const carregarEstatisticas = async () => {
        try {
            // Este endpoint precisa ser criado no back-end
            const response = await fetch('/consultas/dashboard-stats');

            if (response.redirected) {
                window.location.href = response.url;
                return;
            }
            if (!response.ok) {
                throw new Error('Não foi possível carregar as estatísticas.');
            }

            const stats = await response.json();

            // Preenche os cards com os dados recebidos
            document.getElementById('stat-total-produtos').textContent = stats.totalProdutosCadastrados;
            document.getElementById('stat-lotes-ativos').textContent = stats.totalLotesAtivos;
            document.getElementById('stat-proximo-vencimento').textContent = stats.itensProximoVencimento;
            document.getElementById('stat-total-setores').textContent = stats.totalSetoresCadastrados;

        } catch (error) {
            console.error("Erro ao carregar estatísticas do dashboard:", error);
            // Coloca uma mensagem de erro nos cards
            document.getElementById('stat-total-produtos').textContent = 'Erro';
            document.getElementById('stat-lotes-ativos').textContent = 'Erro';
            document.getElementById('stat-proximo-vencimento').textContent = 'Erro';
            document.getElementById('stat-total-setores').textContent = 'Erro';
        }
    };

    // Chama a função para carregar os dados assim que a página estiver pronta
    carregarEstatisticas();
});
