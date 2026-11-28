<div align="center">

# 🌐 Labirinto das Redes — *Packet Escape*

**Um jogo educativo de exploração de redes e gerenciamento de pacotes desenvolvido em Java com Programação Orientada a Objetos (POO) e Java Swing.**

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Java_Swing_%2F_2D-007396?style=for-the-badge&logo=java&logoColor=white)
![POO](https://img.shields.io/badge/Arquitetura-POO_Pura-2EA44F?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Concluído-38BDF8?style=for-the-badge)

</div>

---

## 📖 Sobre o Projeto

No **Labirinto das Redes**, você assume o controle de um **Pacote IP** dentro de uma infraestrutura corporativa sob ataque de *ransomware*. Sua missão é navegar pelos dispositivos de rede, gerenciar sua capacidade de carga (**MTU de 50 bytes**), solucionar desafios de protocolos e escapar pelo **Gateway de Saída** antes que o tempo expire (**240 segundos**) ou o limite de saltos (**90 TTL**) seja atingido.

O projeto foi construído para aplicar na prática conceitos fundamentais de **Programação Orientada a Objetos** e **Redes de Computadores** em uma interface visual estilo *cyberpunk / terminal*.

---

## ✨ Funcionalidades e Mecânicas

* **🗺️ Mapa Interativo em Tempo Real (`Java2D`):** Renderização dinâmica da topologia de rede com enlaces animados, indicadores visuais de bloqueio, alertas de hardware e rastro de movimentação do pacote.
* **📦 Gerenciamento de Inventário com Limite de MTU:** O jogador pode carregar até **50 bytes** simultaneamente, precisando decidir estrategicamente quais pacotes coletar ou descartar.
* **🔐 Salas com Comportamentos Polimórficos:**
  * `LockedRoom`: Exige credenciais específicas (`token` JWT ou `chave` AES-256) para liberar o tráfego.
  * `FaultyRouterRoom`: Hardware superaquecido que penaliza o pacote em **+3 saltos** por retransmissão.
  * `RandomTunnelRoom`: Túnel Proxy NAT instável que redireciona o pacote aleatoriamente pela topologia.
* **🧩 Desafios de Redes:** Bloqueios lógicos que exigem conhecimentos reais de endereçamento IPv4, sub-redes e portas de protocolos seguros (HTTPS/TLS).
* **⌨️ Controle Híbrido (GUI + CLI):** Jogue utilizando as **Setas / WASD**, os botões laterais de ação ou digitando comandos diretamente no terminal integrado (`root@pacote-ip:~#`).

---

## 🎮 Como Jogar

### 1. Regras e Condições de Vitória/Derrota
* **Objetivo:** Chegar à sala **Gateway de Saída** carregando a **`chave`** AES-256 no inventário.
* **Limite de Saltos (TTL):** Você começa com `0` saltos e perde se atingir **90 saltos**. Cada passo no mapa consome `1` salto; errar um desafio aplica `+2` saltos; entrar no Roteador Defeituoso aplica `+3` saltos.
* **Temporizador do Ransomware:** Você tem **240 segundos (4 minutos)** para concluir a transmissão antes que os servidores sejam criptografados.
* **Capacidade MTU:** Seu pacote suporta no máximo **50 bytes** de itens simultâneos.

### 2. Controles
* **Movimentação:** Use as **Setas Direcionais**, as teclas **W, A, S, D** ou digite `ir <norte|sul|leste|oeste>` no terminal inferior.
* **Ações de Inventário:** Clique nos botões da barra lateral direita ou digite:
  * `pegar <item>` — Coleta um item da sala atual (se houver espaço no MTU).
  * `soltar <item>` — Descarta um item no chão da sala para liberar bytes de MTU.
  * `usar <item>` — Ativa o efeito do item (itens consumíveis são removidos após o uso).
  * `inventario` — Lista os itens carregados e a carga atual de bytes.
* **Diagnóstico e Desafios:**
  * `olhar` — Exibe a descrição detalhada, saídas, itens e status de desafio da sala.
  * `responder <resposta>` — Envia a resposta para o desafio ativo da sala.
  * `dica` — Mostra uma pista sobre o desafio atual ou próximo passo.

### 3. Catálogo de Itens

| Item | Peso (MTU) | Localização | Tipo / Efeito |
| :--- | :---: | :--- | :--- |
| `ping` | `5 B` | Estação de Trabalho | **Consumível:** Faz varredura ICMP revelando saídas, itens e alertas na sala. |
| `token` | `15 B` | Corredor Principal | **Chave de Acesso:** Permite atravessar a barreira do **Firewall**. |
| `dump` | `25 B` | Corredor Principal | **Armadilha:** Pacote de broadcast inútil que ocupa metade do MTU. |
| `log` | `10 B` | Roteador Defeituoso | **Informação:** Ao usar (`usar log`), exibe o IP do Gateway (`192.168.10.254`) e a porta segura (`443`). |
| `qos` | `8 B` | Servidor DNS & Cache | **Consumível:** Prioriza o tráfego na rede e recupera **8 saltos (`-8 hops`)**. |
| `chave` | `15 B` | Sala dos Servidores | **Chave Final:** Credencial AES-256 exigida para abrir o **Gateway de Saída**. |

---

## 🗺️ Guia Detalhado: O Que Fazer em Cada Sala e Como Zerar

O mapa é composto por **9 salas** interligadas. Siga o roteiro abaixo para concluir o jogo com folga:

1. **Estação de Trabalho *(Ponto de Partida)*:**
   * **O que fazer:** Opcionalmente digite `pegar ping` (5 B) e siga para o **Leste** (`→`).
2. **Corredor Principal *(Backbone)*:**
   * **O que fazer:** Pegue obrigatoriamente o **`token`** (`pegar token`, 15 B). **Não pegue o `dump` (25 B)**, pois ele desperdiça espaço de MTU.
3. **Roteador Defeituoso *(Ao Sul do Corredor — Opcional)*:**
   * **O que fazer:** Desça para o **Sul** (`↓`) — você sofrerá `+3` saltos de penalidade —, pegue o **`log`** (`pegar log`, 10 B), leia com `usar log` para descobrir as senhas da rede e volte para o **Norte** (`↑`) rumo ao Corredor.
4. **Sala do Switch *(Switch L3 — A Leste do Corredor)*:**
   * **O que fazer:** Siga para o **Leste** (`→`). A passagem para o sul estará bloqueada por um desafio de roteamento. Resolva enviando **`192.168.10.254`** (`responder 192.168.10.254` ou pelo botão `[?] Resolver Desafio`).
5. **Servidor DNS & Cache *(A Leste do Switch — Opcional)*:**
   * **O que fazer:** Se precisar recuperar saltos, vá para o **Leste** (`→`), pegue o **`qos`** (`pegar qos`, 8 B), ative com `usar qos` (`-8` saltos) e retorne ao **Oeste** (`←`) para o Switch.
6. **Firewall *(Ao Sul da Sala do Switch)*:**
   * **O que fazer:** Com o desafio do Switch resolvido e o **`token`** no inventário, desça para o **Sul** (`↓`) para atravessar o Firewall e siga para o **Leste** (`→`). *(Atenção: não desça ao sul do Firewall, pois o **Túnel Proxy NAT** teleporta você aleatoriamente de volta ao início!)*
7. **Sala dos Servidores *(Datacenter — A Leste do Firewall)*:**
   * **O que fazer:** Resolva o desafio de autenticação TLS enviando **`443`** (`responder 443`). Em seguida, colete a **`chave`** (`pegar chave`, 15 B).
8. **Gateway de Saída *(A Leste dos Servidores — Vitória)*:**
   * **O que fazer:** Com a **`chave`** no inventário e o desafio do Datacenter resolvido, avance para o **Leste** (`→`) para escapar para a Nuvem e vencer o jogo!

---

## 🧠 Como o Código Funciona (Arquitetura e POO)

O projeto foi dividido em pacotes lógicos e visuais para garantir baixo acoplamento e alta coesão:

### 1. Estrutura de Diretórios
```text
LabirintoDasRedes/
└── src/
    ├── Main.java                 # Inicializa a janela JFrame na Event Dispatch Thread (Swing)
    ├── Game.java                 # Motor central: gerencia salas, regras, TTL, timer e eventos
    ├── Player.java               # Modela o pacote IP, coordenadas (x, y) e inventário com MTU
    ├── MazeMap.java              # Matriz 13x25 que mapeia tiles caminháveis, paredes e centros das salas
    ├── GamePanel.java            # Interface HUD: barras de progresso, botões laterais e terminal
    ├── MazeCanvas.java           # Renderização gráfica 2D customizada (paintComponent) e animações
    ├── Parser.java               # Lê a entrada textual do terminal e separa verbo + argumento
    ├── Command.java              # Encapsula o comando validado (commandWord e secondWord)
    ├── CommandWords.java         # Dicionário de comandos válidos reconhecidos pelo jogo
    ├── NetworkChallenge.java     # Encapsula perguntas, validação case-insensitive e dicas
    ├── Room.java                 # Classe base dos dispositivos/nós da rede
    │   ├── LockedRoom.java       # Herança: sobrescreve canEnter() exigindo um item específico
    │   ├── FaultyRouterRoom.java # Herança: sobrescreve onEnter() aplicando penalidade de saltos
    │   └── RandomTunnelRoom.java # Herança: sobrescreve onEnter() acionando teleporte aleatório
    └── Item.java                 # Classe abstrata base com nome, descrição, peso (bytes) e use()
        ├── ConsumableItem.java   # Template Method: executa applyEffect() e remove o item após uso
        │   ├── PingItem.java     # Implementa varredura de saídas e itens da sala atual
        │   └── PacketBoosterItem.java # Implementa redução de saltos (QoS)
        ├── TokenItem.java        # Credencial de sessão JWT para o Firewall
        ├── SecurityKey.java      # Chave AES-256 para o Gateway
        ├── CorruptedLogItem.java # Exibe informações interceptadas do roteador
        └── JunkPacketItem.java   # Item de peso morto para testar o gerenciamento de MTU
