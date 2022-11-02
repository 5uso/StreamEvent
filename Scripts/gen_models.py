colors = ["gray", "white", "pink", "purple", "blue", "cyan", "light_blue", "green", "lime", "yellow", "orange", "red"]
color_indexes = [7,15,13,5,9,3,11,2,10,14,6,4]

for c, idx in zip(colors, color_indexes):
    with open(f'{c}_holoblock.json', 'w') as f:
        f.write(f'''{{"parent":"suso:block/holoblock/{c}"}}''')