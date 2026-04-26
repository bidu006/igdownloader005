# IGrab — Android Downloader para Instagram
### Kotlin nativo · Material 3 · gallery-dl backend

---

## Como funciona

O app tem duas partes que trabalham juntas:

```
┌─────────────────────┐         HTTP          ┌──────────────────────────┐
│   IGrab (APK)       │  ←──────────────────→ │  gallery-dl + Flask      │
│   App Android       │   localhost:5000       │  rodando no Termux       │
└─────────────────────┘                        └──────────────────────────┘
```

O app Kotlin é a interface bonita. O Flask+gallery-dl faz o trabalho pesado de download.

---

## Passo 1 — Configurar o backend no Termux

```bash
# Instalar dependências
pkg install python
pip install flask gallery-dl

# Copiar os arquivos do backend para o Termux
# (use o ZIP do ig-downloader que você já tem)
cd ~/ig-downloader
python app.py
```

Deixe rodando em segundo plano (deslize o Termux para baixo).

---

## Passo 2 — Build do APK no Android Studio

### Requisitos
- Android Studio Hedgehog ou mais recente
- JDK 17
- Android SDK 34

### Build
1. Abra o Android Studio
2. File → Open → selecione a pasta `IGrab`
3. Aguarde o Gradle sync terminar
4. Build → Generate Signed APK (ou Run para testar no emulador)

### Build via linha de comando (Linux/Mac)
```bash
cd IGrab
./gradlew assembleDebug
# APK em: app/build/outputs/apk/debug/app-debug.apk
```

---

## Passo 3 — Instalar o APK

```bash
# Via ADB (celular conectado por USB)
adb install app/build/outputs/apk/debug/app-debug.apk

# Ou transfira o APK para o celular e instale manualmente
# (Precisa habilitar "Fontes desconhecidas" nas configurações)
```

---

## Funcionalidades do App

| Feature | Detalhe |
|---|---|
| Cole URL do IG | Posts, Reels, perfis públicos |
| Receber URL compartilhada | Funciona com o botão "Compartilhar" do IG |
| Preview das mídias | Fotos e vídeos baixados aparecem no grid |
| Abrir / Compartilhar | Toque num arquivo para abrir ou compartilhar |
| Cookies | Para Stories e contas privadas |
| Histórico | Room DB local, persiste entre sessões |
| Notificação foreground | Download não morre em background |
| Configurações | URL do servidor configurável |

---

## Estrutura do Projeto

```
IGrab/
├── app/src/main/
│   ├── java/com/igrab/app/
│   │   ├── data/
│   │   │   ├── Models.kt          # Data classes e enums
│   │   │   ├── Database.kt        # Room DB + DAO
│   │   │   ├── GalleryDlClient.kt # Cliente HTTP (OkHttp)
│   │   │   └── Repository.kt      # Repositório + lógica de polling
│   │   ├── ui/
│   │   │   ├── MainActivity.kt    # Activity principal
│   │   │   ├── MainViewModel.kt   # ViewModel (MVVM)
│   │   │   ├── MediaAdapter.kt    # Grid de arquivos baixados
│   │   │   └── HistoryAdapter.kt  # Lista de histórico
│   │   └── service/
│   │       └── DownloadService.kt # Foreground service
│   └── res/
│       ├── layout/                # XMLs de layout
│       ├── drawable/              # Ícones e badges
│       ├── values/                # Cores, strings, temas
│       └── xml/                   # FileProvider paths
└── build.gradle
```

---

## Solução de problemas

**"Servidor offline"**
→ Abra o Termux e rode `python ~/ig-downloader/app.py`

**Arquivo não baixou**
→ Verifique o log no app. Se aparecer erro 401, você precisa de cookies.

**Cookies**
→ No Chrome do PC, instale a extensão "Get cookies.txt LOCALLY"
→ Acesse instagram.com logado e exporte o cookies.txt
→ Cole o conteúdo em Configurações → Cookies no app

**Build falhou no Gradle**
→ Certifique-se de usar JDK 17: File → Project Structure → SDK Location
