import re

def iCounter(fileName):
    counter = {}
    f = open(fileName, "r")
    for line in f:
        matchObj = re.match("(.*)\t(.*)\t(.*?) (.*)", line)
        if matchObj != None:
            instruction = matchObj.group(3)
            if instruction in counter:
                counter[instruction] += 1
            else:
                counter[instruction] = 1
    return counter

def _addUnusedInstructions(counter1, counter2):
    data = counter2.items()
    for field in data:
        if not field[0] in counter1:
            counter1[field[0]] = 0

def addUnusedInstruction(counterList):
    for counter1 in counterList:
        for counter2 in counterList:
            if counter1 != counter2:
                _addUnusedInstructions(counter1, counter2)

def orderByInstruction(counter):
    data = sorted(counter.items(), key=lambda tup: tup[0])
    return data

def orderByUse(counter):
    data = sorted(counter.items(), key=lambda tup: tup[1], reverse=True)
    return data

def exec(fileName1, fileName2):
    counter1 = iCounter(fileName1)
    counter2 = iCounter(fileName2)
    addUnusedInstruction((counter1, counter2))
    counter1 = orderByInstruction(counter1)
    counter2 = orderByInstruction(counter2)
    return (counter1, counter2)
