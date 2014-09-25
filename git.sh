#!/bin/bash

directorio=$1
email=$(git config user.email)
cd $directorio
git init
git add .
git commit -m"[#0] Crear estructura inicial para microservicio"

blackbox_initialize yes
git commit -m"INITIALIZE BLACKBOX" keyrings .gitignore
blackbox_addadmin $email
git commit -m"NEW ADMIN: $email" keyrings/live/pubring.gpg keyrings/live/trustdb.gpg keyrings/live/blackbox-admins.txt
blackbox_register_new_file sniese.keystore
blackbox_register_new_file servicio-dev.yml
blackbox_register_new_file src/integration/resources/test-integracion.yml
blackbox_postdeploy
