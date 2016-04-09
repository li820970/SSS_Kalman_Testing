import csv

values = list()

for i in range (0,50):
    values.append('-100')


with open('idle_1.csv', 'rb') as csvread:
    reader = csv.reader(csvread, delimiter = ',')

    with open('idle_1_1.csv', 'wb') as csvwrite:
        writer = csv.writer(csvwrite, delimiter = ',')
                            
        for row in reader:
            for i in range(0, len(row)):
                #export = list()
                if (row[i] != '-100'):
                    values[i] = row[i]
                row[i] = values[i]
                #export.append(values[i])
            writer.writerow(row)
            
            
            
            
