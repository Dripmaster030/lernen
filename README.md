# Aurora Client v2 — Minecraft Java 1.21.11 / Fabric

**Status: Quellcode, nicht kompiliert oder im Spiel getestet.**

## Neu
- Transparente Panels mit Drag and Drop an der Titelleiste
- Ein und ausklappbar über + / −
- Settings unten mittig mit RGB Farbwahl
- Pro Modul Klick auf KEY zum Belegen, Backspace/Delete zum Löschen
- Persistenz in config/aurora-client.properties
- Experimentelle Fullbright Funktion (Gamma per Reflection; kann abhängig von Minecraft 1.21.11 nicht greifen)

## Einschränkungen
**Custom Glint:** Die Option und ihr Keybind sind vorbereitet, aber die Änderung des tatsächlichen Vanilla Verzauberungsglanzes ist noch nicht implementiert.
**Inspect Item:** Die Option und ihr Keybind sind vorbereitet, die Item Animation noch nicht implementiert.
**Fullbright:** Experimentelle Version, kann funktionieren oder je nach der verfügbaren Gamma API wirkungslos bleiben.

Das ist bewusst dokumentiert, damit keine leeren Schalter als fertige Funktionen verkauft werden.

## GitHub
Lade die Inhalte dieses Ordners direkt in die oberste Ebene deines Repositories hoch.
Die Workflow Datei ist `.github/workflows/build.yml`.
Dann unter Actions den Build ausführen.
Bei einem Compilerfehler bitte die gesamte Fehlermeldung schicken.
