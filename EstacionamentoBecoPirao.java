import javax.swing.*;
import java.util.*;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class EstacionamentoBecoPirao {

    private static final int CAPACIDADE = 10;
    private Deque<Integer> estacionamento = new LinkedList<>();
    private Queue<Integer> filaEspera = new LinkedList<>();

    public void chegadaCarro(int placa, boolean entradaPelaRBC) {
        if (estacionamento.size() < CAPACIDADE) {
            if (entradaPelaRBC) {
                estacionamento.addFirst(placa);
                JOptionPane.showMessageDialog(null, "Carro " + placa + " estacionou pela RBC.");
            } else {
                estacionamento.addLast(placa);
                JOptionPane.showMessageDialog(null, "Carro " + placa + " estacionou pela PT.");
            }
        } else {
            filaEspera.add(placa);
            JOptionPane.showMessageDialog(null, "Estacionamento cheio. Carro " + placa + " aguardando vaga.");
        }
        exibirEstacionamento();
    }

    public void partidaCarro(int placa) {
        if (!estacionamento.contains(placa)) {
            if (filaEspera.contains(placa)) {
                filaEspera.remove(placa);
                JOptionPane.showMessageDialog(null, "Carro " + placa + " saiu da fila de espera. Número de deslocamentos: 0");
            } else {
                JOptionPane.showMessageDialog(null, "Carro " + placa + " não está no estacionamento ou na fila de espera.");
            }
            return;
        }

        int deslocamentos = 0;
        List<Integer> carrosDeslocados = new ArrayList<>();

        while (!estacionamento.isEmpty() && estacionamento.peekLast() != placa) {
            carrosDeslocados.add(estacionamento.removeLast());
            deslocamentos++;
        }

        if (!estacionamento.isEmpty()) {
            estacionamento.removeLast();
            deslocamentos++;
            JOptionPane.showMessageDialog(null, "Carro " + placa + " saiu após " + deslocamentos + " deslocamentos.");
        }

        for (int i = carrosDeslocados.size() - 1; i >= 0; i--) {
            estacionamento.addLast(carrosDeslocados.get(i));
        }

        if (!filaEspera.isEmpty() && estacionamento.size() < CAPACIDADE) {
            int novoCarro = filaEspera.poll();
            estacionamento.addLast(novoCarro);
            JOptionPane.showMessageDialog(null, "Carro " + novoCarro + " entrou do estado de espera.");
        }

        exibirEstacionamento();
    }

    private void exibirEstacionamento() {
        StringBuilder sb = new StringBuilder("Estacionamento: \n");
        for (int placa : estacionamento) {
            sb.append("[ ").append(placa).append(" ]");
        }

        if (estacionamento.isEmpty()) {
            sb.append("Vazio");
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Situação do Estacionamento", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        EstacionamentoBecoPirao estacionamento = new EstacionamentoBecoPirao();

        while (true) {
            String[] opcoes = {"Chegada pela PT", "Chegada pela RBC", "Partida", "Sair"};
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Escolha uma operação:",
                    "Estacionamento Beco do Pirão",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            if (escolha == 3) {
                JOptionPane.showMessageDialog(null, "Encerrando o programa.");
                break;
            }

            String placaStr = JOptionPane.showInputDialog("Digite a placa do carro:");
            if (placaStr == null || placaStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Placa inválida. Tente novamente.");
                continue;
            }

            int placa;
            try {
                placa = Integer.parseInt(placaStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Placa inválida! Digite um número.");
                continue;
            }

            if (escolha == 0) {
                estacionamento.chegadaCarro(placa, false);
            } else if (escolha == 1) {
                estacionamento.chegadaCarro(placa, true);
            } else if (escolha == 2) {
                estacionamento.partidaCarro(placa);
            }
        }
    }
}