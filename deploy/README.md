# Production deployment

Runs on a single AWS EC2 `t4g.nano` (ARM Graviton) instance in front of Caddy for TLS. GitHub Actions (`../.github/workflows/deploy.yml`) builds the Docker image, pushes it to GHCR, then SSHes into the host to roll out the new tag.

## One-time host bootstrap

Run these on the EC2 host (Amazon Linux 2023, arm64):

```bash
# Docker
sudo dnf install -y docker
sudo systemctl enable --now docker
sudo usermod -aG docker ec2-user
newgrp docker  # or re-login

# Caddy
sudo dnf install -y 'dnf-command(copr)'
sudo dnf copr enable -y @caddy/caddy
sudo dnf install -y caddy

# Deploy dir owned by ec2-user
sudo mkdir -p /opt/ac-api
sudo chown ec2-user:ec2-user /opt/ac-api

# Copy config into place (from your laptop):
#   scp deploy/docker-compose.yml ec2-user@<host>:/opt/ac-api/docker-compose.yml
#   scp deploy/Caddyfile ec2-user@<host>:/tmp/Caddyfile
#   ssh ec2-user@<host> "sudo mv /tmp/Caddyfile /etc/caddy/Caddyfile && sudo systemctl enable --now caddy"

# SSH keypair for GitHub Actions deploy
ssh-keygen -t ed25519 -f ~/.ssh/gha_deploy -N ""
cat ~/.ssh/gha_deploy.pub >> ~/.ssh/authorized_keys
cat ~/.ssh/gha_deploy   # <-- paste into GitHub secret EC2_DEPLOY_KEY
```

## GitHub secrets required

Under Settings → Secrets and variables → Actions:

- **`EC2_HOST_ADDR`** — IP or hostname, e.g. `203.0.113.42`
- **`EC2_HOST_USER`** — SSH user, `ec2-user`
- **`EC2_DEPLOY_KEY`** — contents of `~/.ssh/gha_deploy` (the *private* key)

## DNS

`ac.jolgorio.app` A record → EC2 Elastic IP (TTL 300).

## Verify

```bash
curl https://ac.jolgorio.app/health
# {"status":"ok"}
```
