import iCounter
import matplotlib.pyplot as plt

'''Pega a quantidade de execucoes para cada instrucao, sendo nao_otimizado o programa nao otimizado e otimizado o programa otimizado'''
example = 1
print("example{}.c".format(example))
(nao_otimizado, otimizado) = iCounter.exec("example{}_o0.dump".format(example), "example{}_o3.dump".format(example))
# print("nao_otimizado: {}".format(nao_otimizado))
# print()
# print("otimizado: {}".format(otimizado))
# print()


'''Pega cada instrucao para o eixo x do histograma'''
eixo_x = []
for i in range(len(nao_otimizado)):
    eixo_x.append(nao_otimizado[i][0])
print(eixo_x)

'''Pega a quantidade de execucoes para cada instrucao'''
nao_otimizado_y = []
otimizado_y = []
for i in range(len(otimizado)):
    nao_otimizado_y.append(nao_otimizado[i][1])
    otimizado_y.append(otimizado[i][1])
print(nao_otimizado_y)
print(otimizado_y)

plt.hist(x=[1,2,3])
plt.show()
