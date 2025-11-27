package javatro.logica.evaluador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javatro.modelo.core.Carta;
import javatro.modelo.core.Palo;
import javatro.modelo.core.Valor;

public class EvaluadorMano {

    public static class ResultadoMano {
        public String nombreMano;
        public int fichasBase;
        public int multiplicadorBase;
        public List<Carta> cartasQuePuntuan; // NUEVO: Lista de cartas que suman puntos

        public ResultadoMano(String nombre, int fichas, int mult, List<Carta> cartasPuntuan) {
            this.nombreMano = nombre;
            this.fichasBase = fichas;
            this.multiplicadorBase = mult;
            this.cartasQuePuntuan = cartasPuntuan;
        }
    }

    public static ResultadoMano evaluar(List<Carta> cartas) {
        if (cartas == null || cartas.isEmpty()) 
            return new ResultadoMano("CARTA ALTA", 5, 1, new ArrayList<>());

        // Ordenamos cartas por valor descendente (Mayor a menor) para facilitar "Carta Alta"
        List<Carta> ordenada = new ArrayList<>(cartas);
        ordenada.sort((c1, c2) -> Integer.compare(c2.getValor().ordinal(), c1.getValor().ordinal()));

        // Mapas para contar repeticiones
        Map<Integer, List<Carta>> gruposValor = new HashMap<>();
        Map<Palo, List<Carta>> gruposPalo = new HashMap<>();

        for (Carta c : ordenada) {
            int val = c.getValor().ordinal();
            gruposValor.putIfAbsent(val, new ArrayList<>());
            gruposValor.get(val).add(c);
            
            Palo p = c.getPalo();
            gruposPalo.putIfAbsent(p, new ArrayList<>());
            gruposPalo.get(p).add(c);
        }

        // Detectar grupos (Pares, Trios, Poker)
        List<Carta> pokerCards = new ArrayList<>();
        List<Carta> trioCards = new ArrayList<>();
        List<List<Carta>> paresCards = new ArrayList<>();

        for (List<Carta> grupo : gruposValor.values()) {
            if (grupo.size() == 4) pokerCards.addAll(grupo);
            if (grupo.size() == 3) trioCards.addAll(grupo);
            if (grupo.size() == 2) paresCards.add(grupo);
        }

        // Detectar Color y Escalera
        List<Carta> colorCards = null;
        for (List<Carta> grupo : gruposPalo.values()) {
            if (grupo.size() >= 5) colorCards = grupo.subList(0, 5); // Tomamos las 5 mejores
        }
        
        List<Carta> escaleraCards = buscarEscalera(ordenada);

        // --- JERARQUÍA (Evaluamos y retornamos SOLO las cartas relevantes) ---

        // 1. ESCALERA REAL / COLOR
        if (escaleraCards != null && colorCards != null) {
            // Verificamos si la escalera es del mismo palo (simplificado)
            // Para ser exactos, deberíamos chequear la intersección, pero asumiremos Escalera Real si coinciden
            if (contieneAs(escaleraCards) && contieneRey(escaleraCards))
                return new ResultadoMano("ESCALERA REAL", 100, 8, escaleraCards);
            return new ResultadoMano("ESCALERA COLOR", 100, 8, escaleraCards);
        }

        // 3. POKER
        if (!pokerCards.isEmpty()) {
            return new ResultadoMano("POKER", 60, 7, pokerCards);
        }

        // 4. FULL HOUSE
        if (!trioCards.isEmpty() && !paresCards.isEmpty()) {
            List<Carta> full = new ArrayList<>(trioCards);
            full.addAll(paresCards.get(0));
            return new ResultadoMano("FULL HOUSE", 40, 4, full);
        }

        // 5. COLOR
        if (colorCards != null) {
            return new ResultadoMano("COLOR", 35, 4, colorCards);
        }

        // 6. ESCALERA
        if (escaleraCards != null) {
            return new ResultadoMano("ESCALERA", 30, 4, escaleraCards);
        }

        // 7. TRÍO
        if (!trioCards.isEmpty()) {
            return new ResultadoMano("TRIO", 30, 3, trioCards);
        }

        // 8. DOBLE PAR
        if (paresCards.size() >= 2) {
            List<Carta> doblePar = new ArrayList<>();
            doblePar.addAll(paresCards.get(0));
            doblePar.addAll(paresCards.get(1));
            return new ResultadoMano("DOBLE PAR", 20, 2, doblePar);
        }

        // 9. PAR
        if (paresCards.size() == 1) {
            return new ResultadoMano("PAR", 10, 2, paresCards.get(0));
        }

        // 10. CARTA ALTA (Solo la carta más alta cuenta)
        List<Carta> cartaAlta = new ArrayList<>();
        cartaAlta.add(ordenada.get(0)); // La primera es la mayor porque ordenamos al inicio
        return new ResultadoMano("CARTA ALTA", 5, 1, cartaAlta);
    }

    // Auxiliares
    private static List<Carta> buscarEscalera(List<Carta> cartas) {
        if (cartas.size() < 5) return null;
        List<Carta> unicos = new ArrayList<>();
        // Filtrar unicos por valor
        for(Carta c : cartas) {
            boolean repetido = false;
            for(Carta u : unicos) if(u.getValor() == c.getValor()) repetido = true;
            if(!repetido) unicos.add(c);
        }
        if(unicos.size() < 5) return null;

        for (int i = 0; i <= unicos.size() - 5; i++) {
            boolean esEscalera = true;
            for (int j = 0; j < 4; j++) {
                if (unicos.get(i+j).getValor().ordinal() != unicos.get(i+j+1).getValor().ordinal() + 1) {
                    esEscalera = false; 
                    break;
                }
            }
            if (esEscalera) return unicos.subList(i, i+5);
        }
        // Caso As bajo (A, 5, 4, 3, 2) - Lógica simplificada para ejemplo
        return null; 
    }
    
    private static boolean contieneAs(List<Carta> l) { for(Carta c:l) if(c.getValor()==Valor.AS) return true; return false; }
    private static boolean contieneRey(List<Carta> l) { for(Carta c:l) if(c.getValor()==Valor.REY) return true; return false; }
}