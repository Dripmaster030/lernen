# Aurora Client GUI — Java 1.21.11 / Fabric

Ein rein clientseitiges, modernes Minecraft GUI mit **rechter Shift Taste** zu öffnen.

## Enthalten
* Dunkles Menü mit Seitenleiste, Visuals, HUD, Movement und **Settings**.
* In **Settings** fünf Farbvoreinstellungen sowie eigene RGB Anpassung in Zehnerschritten.
* Farbe wird automatisch in `config/aurora-client.properties` gespeichert.
* Die übrigen Modulschalter sind **nur Demo** und haben noch keine Gameplay Funktion.

## Als Modrinth Mod bauen
1. Java JDK 21 und Gradle installieren und das ZIP entpacken.
2. Projekt als Gradle Projekt in IntelliJ öffnen (oder Terminal im entpackten Projekt).
3. `gradle build` ausführen. Die Abhängigkeiten werden dabei heruntergeladen.
4. Die erzeugte normale JAR aus `build/libs` in Modrinth App → dein Profil → Ordner öffnen → `mods` kopieren.
5. Für das Profil **Fabric 1.21.11** auswählen, Fabric API für 1.21.11 installieren und Minecraft starten.
6. **Rechte Shift Taste** zum Öffnen drücken.

**Wichtig:** Dies ist Quellcode, keine fertig kompilierte JAR. Die Kompilierung konnte in dieser Umgebung ohne Gradle und Internetzugriff nicht durchgeführt werden, daher ist Laufzeitkompatibilität noch nicht verifiziert.
