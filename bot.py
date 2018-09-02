import discord

client = discord.Client()

@client.event
async def on_ready():
    print('Logged in as')
    print(client.user.name)
    print(client.user.id)
    print('------')

@client.event
async def on_member_join(member):
    if member is not None:
        embed = discord.Embed(title="Bienvenue sur le serveur non officiel de l'ISTIC !", description="Pour obtenir l'accès complet à votre promotion, aller dans le salon accueil ", color=0x01B8EA)
        embed.set_image(url='https://istic.univ-rennes1.fr/sites/istic.univ-rennes1.fr/files/logoisticfr_0.png')
        await client.send_message(member, embed=embed)


client.run('token')